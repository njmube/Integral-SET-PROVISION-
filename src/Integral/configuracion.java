/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Usuario;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import org.hibernate.Session;

/**
 *
 * @author salvador
 */
public class configuracion extends javax.swing.JPanel {

    String sessionPrograma="";
    Herramientas h;
    Usuario usr;
    File f_logo;
    int entro_logo=0;
    
    File f_sello;
    int entro_sello=0;
    
    File f_cinta;
    int entro_cinta=0;
    
    File f_factura;
    int entro_factura=0;
    javax.swing.JLabel l_nombre;
    javax.swing.JPanel p_img;
    JFileChooser selector;
    String ruta="";
    /**
     * Creates new form configuracion
     */
    public configuracion(Usuario usuario, String ses, javax.swing.JLabel l, javax.swing.JPanel p_img) {
        initComponents();
        try{
            FileReader fil = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(fil);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
            fil.close();
            fil=null;
            b=null;
        }catch(Exception e){}
        l_nombre=l;
        this.p_img=p_img;
        sessionPrograma=ses;
        usr=usuario;
        busca(1);
    }

    public void busca(int no)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Configuracion con = (Configuracion)session.get(Configuracion.class, no);
            if(con!=null)
            {
                p_logo.add(new Imagen("imagenes/"+con.getLogo(), 232, 158, 1, 1,231, 157));//logo
                p_logo.repaint();
                p_sello.add(new Imagen("imagenes/"+con.getSello(), 232, 158, 1, 1,231, 157));//sello
                p_sello.repaint();
                p_cinta.add(new Imagen("imagenes/"+con.getCinta(), 554, 158, 1, 1, 555, 157));//cinta
                p_cinta.repaint();
                p_factura.add(new Imagen("imagenes/factura300115.jpg", 554, 158, 1, 1, 555, 157));//factura
                p_factura.repaint();
                c_empresa.setSelectedItem(con.getEmpresa());
                t_requestor.setText(con.getRequestor());
                t_rfc_emisor.setText(con.getRfc());
                t_usuario.setText(con.getUsuario_1());
                t_razon_social.setText(con.getNombre());
                t_direccion.setText(con.getDireccion());
                t_colonia.setText(con.getColonia());
                c_estado.setSelectedItem(con.getEstado());
                t_contacto.setText(con.getContacto());
                t_cp.setText(con.getCp());
                t_numero.setText(con.getNo());
                t_municipio.setText(con.getMunicipio());
                c_pais.setSelectedItem(con.getPais());
                t_mail.setText(con.getMail());
                t_iva.setText(""+con.getIva());
                t_sucursal.setText(con.getSucursal());
                if(con.getTel()!=null)
                    t_tel.setText(con.getTel());
                else
                    t_tel.setText("");
                t_usuario_finkok.setText(con.getEmailFinkok());
                t_clave_finkok.setText(con.getClaveFinkok());
                if(con.getCer()!=null)
                    t_cer.setText(con.getCer());
                else
                    t_cer.setText("");
                if(con.getLlave()!=null)
                    t_key.setText(con.getLlave());
                else
                    t_key.setText("");
                if(con.getClave()!=null)
                    t_clave.setText(con.getClave());
                else
                    t_clave.setText("");
                
                switch(con.getPac())
                {
                    case "MYSUITE":
                        rb_mysuite.setSelected(true);
                        rb_finkok.setSelected(false);
                        break;
                    case "FINKOK":
                        rb_finkok.setSelected(true);
                        rb_mysuite.setSelected(false);
                        break;
                    default:
                        rb_mysuite.setSelected(true);
                        rb_finkok.setSelected(false);
                        break;
                }
                if(con.getSerie()!=null)
                t_serie.setText(con.getSerie());
                t_serie1.setText(con.getSerie1());
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    
    private boolean guardaFoto(File origen, File destino, int x, int y, String extencion)
    {
        try 
        {
            if(origen.exists()==true)
            {
                String ruta=origen.getPath();
                javax.swing.JPanel p=new Imagen(ruta, x, y, 0, 0, x, y);
                BufferedImage dibujo =new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
                Graphics g = dibujo.getGraphics();
                p.paint(g);
                ImageIO.write((RenderedImage)dibujo, extencion, destino); // Salvar la imagen en el fichero*/
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupo = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        pestana = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        p_cinta = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        p_factura = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        p_logo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        p_sello = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        t_iva = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        t_requestor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        t_rfc_emisor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        t_usuario = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        t_sucursal = new javax.swing.JTextField();
        rb_mysuite = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        t_usuario_finkok = new javax.swing.JTextField();
        t_clave_finkok = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        t_cer = new javax.swing.JTextField();
        t_key = new javax.swing.JTextField();
        l_cer = new javax.swing.JLabel();
        l_key = new javax.swing.JLabel();
        t_clave = new javax.swing.JTextField();
        l_key1 = new javax.swing.JLabel();
        t_serie = new javax.swing.JTextField();
        t_serie1 = new javax.swing.JTextField();
        l_key2 = new javax.swing.JLabel();
        rb_finkok = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        t_razon_social = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        t_direccion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        t_cp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        t_municipio = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        c_estado = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        t_colonia = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        t_numero = new javax.swing.JTextField();
        c_pais = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        t_contacto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        t_mail = new javax.swing.JTextField();
        t_tel = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        c_empresa = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(254, 254, 254));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Configuración del Sistema");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(13, 132, 254));
        jLabel2.setText("Nombre de la Empresa:");

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setForeground(new java.awt.Color(254, 254, 254));
        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel3.setBackground(new java.awt.Color(254, 254, 254));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Cinta de reportes", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        p_cinta.setBackground(new java.awt.Color(204, 255, 255));
        p_cinta.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        p_cinta.setToolTipText("Agregar imagen de la unidad");
        p_cinta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_cinta.setPreferredSize(new java.awt.Dimension(87, 94));
        p_cinta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_cintaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_cintaLayout = new javax.swing.GroupLayout(p_cinta);
        p_cinta.setLayout(p_cintaLayout);
        p_cintaLayout.setHorizontalGroup(
            p_cintaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
        );
        p_cintaLayout.setVerticalGroup(
            p_cintaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_cinta, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_cinta, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Cinta de Facturas", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        p_factura.setBackground(new java.awt.Color(204, 255, 255));
        p_factura.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        p_factura.setToolTipText("Agregar imagen de la unidad");
        p_factura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_factura.setPreferredSize(new java.awt.Dimension(87, 94));
        p_factura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_facturaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_facturaLayout = new javax.swing.GroupLayout(p_factura);
        p_factura.setLayout(p_facturaLayout);
        p_facturaLayout.setHorizontalGroup(
            p_facturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
        );
        p_facturaLayout.setVerticalGroup(
            p_facturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_factura, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_factura, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Logo de la empresa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        p_logo.setBackground(new java.awt.Color(204, 255, 255));
        p_logo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        p_logo.setToolTipText("Agregar imagen de la unidad");
        p_logo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_logo.setPreferredSize(new java.awt.Dimension(87, 94));
        p_logo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_logoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_logoLayout = new javax.swing.GroupLayout(p_logo);
        p_logo.setLayout(p_logoLayout);
        p_logoLayout.setHorizontalGroup(
            p_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        p_logoLayout.setVerticalGroup(
            p_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_logo, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_logo, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Sello de la empresa", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        p_sello.setBackground(new java.awt.Color(204, 255, 255));
        p_sello.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        p_sello.setToolTipText("Agregar imagen de la unidad");
        p_sello.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_sello.setPreferredSize(new java.awt.Dimension(87, 94));
        p_sello.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_selloMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_selloLayout = new javax.swing.GroupLayout(p_sello);
        p_sello.setLayout(p_selloLayout);
        p_selloLayout.setHorizontalGroup(
            p_selloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        p_selloLayout.setVerticalGroup(
            p_selloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_sello, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_sello, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pestana.addTab("Multimedia", jPanel5);

        jPanel6.setBackground(new java.awt.Color(254, 254, 254));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(13, 132, 254));
        jLabel5.setText("IVA:");

        t_iva.setBackground(new java.awt.Color(204, 255, 255));
        t_iva.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_iva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_ivaKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_ivaKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("%");

        jPanel7.setBackground(new java.awt.Color(254, 254, 254));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Proveedor de Facturación", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(13, 132, 254));
        jLabel7.setText("Requestor:");

        t_requestor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_requestorKeyTyped(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(13, 132, 254));
        jLabel3.setText("RFC del Emisor:");

        t_rfc_emisor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_rfc_emisorKeyTyped(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(13, 132, 254));
        jLabel4.setText("Usuario:");

        t_usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_usuarioKeyTyped(evt);
            }
        });

        jLabel18.setForeground(new java.awt.Color(13, 132, 254));
        jLabel18.setText("Sucursal");

        t_sucursal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_sucursalKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_requestor, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(jLabel18)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                            .addComponent(t_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(t_rfc_emisor, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                .addComponent(t_usuario)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(t_requestor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(t_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(t_rfc_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(t_sucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rb_mysuite.setBackground(new java.awt.Color(255, 255, 255));
        grupo.add(rb_mysuite);
        rb_mysuite.setText("MYSUITE");

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(13, 132, 254));
        jLabel20.setText("Email:");

        jLabel21.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(13, 132, 254));
        jLabel21.setText("Contraseña:");

        t_usuario_finkok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_usuario_finkokKeyTyped(evt);
            }
        });

        t_clave_finkok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_clave_finkokKeyTyped(evt);
            }
        });

        jButton2.setText("CER");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("KEY");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        t_cer.setEditable(false);

        t_key.setEditable(false);

        l_key.setText("Clave:");

        l_key1.setText("Serie F:");

        t_serie.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_serieFocusLost(evt);
            }
        });
        t_serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_serieKeyTyped(evt);
            }
        });

        t_serie1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_serie1FocusLost(evt);
            }
        });
        t_serie1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_serie1KeyTyped(evt);
            }
        });

        l_key2.setText("Serie N:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_usuario_finkok))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_clave_finkok))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cer, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_cer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_key1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_key2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_serie1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_key, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(l_key)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_clave, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(t_usuario_finkok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(t_clave_finkok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_key2)
                        .addComponent(t_serie1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(t_cer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_cer)
                        .addComponent(l_key1)
                        .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(t_key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_key)
                    .addComponent(t_clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        rb_finkok.setBackground(new java.awt.Color(255, 255, 255));
        grupo.add(rb_finkok);
        rb_finkok.setText("FINKOK");
        rb_finkok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_finkokActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rb_mysuite)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(rb_finkok)
                        .addContainerGap(274, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_mysuite)
                    .addComponent(rb_finkok))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(254, 254, 254));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Datos del Emisor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        jLabel8.setText("Razón social:");

        t_razon_social.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_razon_social.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_razon_socialKeyTyped(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(13, 132, 254));
        jLabel9.setText("Dirección:");

        t_direccion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_direccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_direccionKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(13, 132, 254));
        jLabel10.setText("CP:");

        t_cp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_cpKeyTyped(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(13, 132, 254));
        jLabel11.setText("Municipio:");

        t_municipio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_municipio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_municipioKeyTyped(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(13, 132, 254));
        jLabel12.setText("Pais:");

        c_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Distrito Federal", "Durango", "Estado de México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));

        jLabel13.setForeground(new java.awt.Color(13, 132, 254));
        jLabel13.setText("Estado:");

        t_colonia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_colonia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_coloniaKeyTyped(evt);
            }
        });

        jLabel14.setText("Colonia:");

        jLabel15.setText("No Externo");

        t_numero.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_numeroKeyTyped(evt);
            }
        });

        c_pais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MEXICO" }));
        c_pais.setSelectedItem("MX");

        jLabel16.setText("Contacto:");

        t_contacto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_contacto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_contactoKeyTyped(evt);
            }
        });

        jLabel17.setText("Mail:");

        t_mail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_mail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_mailKeyTyped(evt);
            }
        });

        t_tel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_telKeyTyped(evt);
            }
        });

        jLabel19.setText("Tel:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(129, 129, 129))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel8))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(t_razon_social, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                                            .addComponent(t_direccion)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel12))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(t_municipio, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(c_pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(119, 119, 119)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(58, 58, 58))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_contacto, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_mail, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tel, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(t_razon_social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(t_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(t_municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(t_mail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(t_contacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(t_tel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(t_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(t_iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        pestana.addTab("Contabilidad", jPanel6);

        c_empresa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SERVICIO ESPECIALIZADO TOLUCA S.A. DE C.V.", "TRUCK BODY SHOP S.A. DE C.V." }));
        c_empresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_empresaItemStateChanged(evt);
            }
        });
        c_empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_empresaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(pestana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(c_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pestana)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void t_ivaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ivaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_iva.getText().length()>=3)
            evt.consume();
        if((car<'0' || car>'9'))
            evt.consume();
    }//GEN-LAST:event_t_ivaKeyTyped

    private void p_logoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_logoMouseClicked
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        JFileChooser selector=new JFileChooser();
        selector.setFileFilter(new ExtensionFileFilter("JPG and JPEG", new String[] { "JPG", "JPEG" }));
        int estado=1;
        estado=selector.showOpenDialog(null);
        if(estado==0)
        {
            f_logo=selector.getSelectedFile();
            try
            {
                if(f_logo.exists())
                {
                    String ruta=f_logo.getPath();
                    p_logo.removeAll();
                    p_logo.add(new Imagen(ruta, 231, 157, 1, 1,232, 158));
                    p_logo.repaint();
                    entro_logo=1;
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "no se pudo cargar la imagen");
                }
            }catch(Exception e){}
        }
    }//GEN-LAST:event_p_logoMouseClicked

    private void p_selloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_selloMouseClicked
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        JFileChooser selector=new JFileChooser();
        selector.setFileFilter(new ExtensionFileFilter("*.PNG", new String[] { "PNG" }));
        int estado=1;
        estado=selector.showOpenDialog(null);
        if(estado==0)
        {
            f_sello=selector.getSelectedFile();
            try
            {
                if(f_sello.exists())
                {
                    String ruta=f_sello.getPath();
                    p_sello.removeAll();
                    p_sello.add(new Imagen(ruta, 231, 157, 1, 1,232, 158));
                    p_sello.repaint();
                    entro_sello=1;
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "no se pudo cargar la imagen");
                }
            }catch(Exception e){}
        }
    }//GEN-LAST:event_p_selloMouseClicked

    private void p_cintaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_cintaMouseClicked
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        JFileChooser selector=new JFileChooser();
        selector.setFileFilter(new ExtensionFileFilter("JPG and JPEG", new String[] { "JPG", "JPEG" }));
        int estado=1;
        estado=selector.showOpenDialog(null);
        if(estado==0)
        {
            f_cinta=selector.getSelectedFile();
            try
            {
                if(f_cinta.exists())
                {
                    String ruta=f_cinta.getPath();
                    p_cinta.removeAll();
                    p_cinta.add(new Imagen(ruta, 554, 158, 1, 1, 555, 157));
                    p_cinta.repaint();
                    entro_cinta=1;
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "no se pudo cargar la imagen");
                }
            }catch(Exception e){}
        }
    }//GEN-LAST:event_p_cintaMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(t_requestor.getText().compareTo("")!=0)
        {
            if(t_rfc_emisor.getText().compareTo("")!=0)
            {
                if(t_usuario.getText().compareTo("")!=0)
                {
                    if(t_sucursal.getText().compareTo("")!=0)
                    {
                        if(t_direccion.getText().compareTo("")!=0)
                        {
                            if(t_cp.getText().compareTo("")!=0)
                            {
                                if(t_municipio.getText().compareTo("")!=0)
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        session.beginTransaction().commit();
                                        this.usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                        Hibernate.entidades.Configuracion con = (Hibernate.entidades.Configuracion)session.get(Hibernate.entidades.Configuracion.class, c_empresa.getSelectedIndex()+1);
                                        if(con!=null)
                                        {
                                            con.setUsuario(usr);
                                            con.setIva(Integer.parseInt(t_iva.getText()));
                                            con.setRequestor(t_requestor.getText());
                                            con.setRfc(t_rfc_emisor.getText());
                                            con.setUsuario_1(t_usuario.getText());//usuario de mysuite
                                            con.setNombre(t_razon_social.getText());
                                            con.setCp(t_cp.getText());
                                            con.setDireccion(t_direccion.getText());
                                            con.setNo(t_numero.getText());
                                            con.setColonia(t_colonia.getText());
                                            con.setMunicipio(t_municipio.getText());
                                            con.setEstado(c_estado.getSelectedItem().toString());
                                            con.setPais(c_pais.getSelectedItem().toString());
                                            con.setContacto(t_contacto.getText());
                                            con.setMail(t_mail.getText());
                                            con.setTel(t_tel.getText());
                                            con.setSucursal(t_sucursal.getText());
                                            con.setEmailFinkok(t_usuario_finkok.getText());
                                            con.setClaveFinkok(t_clave_finkok.getText());
                                            con.setCer(t_cer.getText());
                                            con.setLlave(t_key.getText());
                                            con.setClave(t_clave.getText());
                                            con.setSerie(t_serie.getText());
                                            con.setSerie1(t_serie1.getText());
                                            if(rb_mysuite.isSelected()==true)
                                                con.setPac("MYSUITE");
                                            else
                                                con.setPac("FINKOK");
                                            session.update(con);
                                            if(this.entro_logo==1)
                                            {
                                                System.out.println(guardaFoto(f_logo, new File("imagenes/"+con.getLogo()), 451, 210, "jpg"));
                                            }
                                            if(this.entro_sello==1)
                                            {
                                                System.out.println(guardaFoto(f_sello, new File("imagenes/"+con.getSello()), 540, 300, "png"));
                                            }
                                            if(this.entro_cinta==1)
                                            {
                                                System.out.println(guardaFoto(f_cinta, new File("imagenes/"+con.getCinta()), 771, 110, "jpg"));
                                            }
                                            if(this.entro_factura==1)
                                            {
                                                System.out.println(guardaFoto(f_cinta, new File("imagenes/factura300115.jpg"), 771, 110, "jpg"));
                                            }
                                            session.beginTransaction().commit();
                                            p_img.updateUI();
                                            javax.swing.JOptionPane.showMessageDialog(null, "Datos almacenados");
                                        }
                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        javax.swing.JOptionPane.showMessageDialog(null, "no se pudo actualizar");
                                    }
                                    if(session != null)
                                        if(session.isOpen())
                                            session.close();
                                }
                                else
                                {
                                    javax.swing.JOptionPane.showMessageDialog(null, "El municipio no puede estar vacio");
                                    pestana.setSelectedIndex(1);
                                    t_numero.requestFocus();
                                }
                            }
                            else
                            {
                                javax.swing.JOptionPane.showMessageDialog(null, "El CP no puede estar vacio");
                                pestana.setSelectedIndex(1);
                                t_cp.requestFocus();
                            }
                        }
                        else
                        {
                            javax.swing.JOptionPane.showMessageDialog(null, "La dirección no puede estar vacio");
                            pestana.setSelectedIndex(1);
                            t_direccion.requestFocus();
                        }
                    }
                    else
                    {
                        javax.swing.JOptionPane.showMessageDialog(null, "La sucursal no puede estar vacia");
                        pestana.setSelectedIndex(1);
                        t_direccion.requestFocus();
                    }
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "en Usuario de mysuite no puede estar vacio");
                    pestana.setSelectedIndex(1);
                    t_usuario.requestFocus();
                }
            }
            else
            {
                javax.swing.JOptionPane.showMessageDialog(null, "El RFC del emisor no puede estar vacio");
                pestana.setSelectedIndex(1);
                t_rfc_emisor.requestFocus();
            }
        }
        else
        {
            javax.swing.JOptionPane.showMessageDialog(null, "El requestor no puede estar vacio");
            pestana.setSelectedIndex(1);
            t_requestor.requestFocus();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void t_ivaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ivaKeyReleased
        // TODO add your handling code here:
        if(t_iva.getText().compareTo("")!=0)
        {
            if(Integer.parseInt(t_iva.getText())>100 || Integer.parseInt(t_iva.getText())<0)
                t_iva.setText("0");
        }
        else
        {
            t_iva.setText("0");
        }
    }//GEN-LAST:event_t_ivaKeyReleased

    private void p_facturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_facturaMouseClicked
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        JFileChooser selector=new JFileChooser();
        selector.setFileFilter(new ExtensionFileFilter("JPG and JPEG", new String[] { "JPG", "JPEG" }));
        int estado=1;
        estado=selector.showOpenDialog(null);
        if(estado==0)
        {
            f_factura=selector.getSelectedFile();
            try
            {
                if(f_factura.exists())
                {
                    String ruta=f_factura.getPath();
                    p_factura.removeAll();
                    p_factura.add(new Imagen(ruta, 554, 158, 1, 1, 555, 157));
                    p_factura.repaint();
                    entro_factura=1;
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "no se pudo cargar la imagen");
                }
            }catch(Exception e){}
        }
    }//GEN-LAST:event_p_facturaMouseClicked

    private void t_razon_socialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_razon_socialKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_razon_social.getText().length()>=200)
        evt.consume();
    }//GEN-LAST:event_t_razon_socialKeyTyped

    private void t_direccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_direccionKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_direccion.getText().length()>=200)
        evt.consume();
    }//GEN-LAST:event_t_direccionKeyTyped

    private void t_cpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_cpKeyTyped
        char car = evt.getKeyChar();
        if(t_cp.getText().length()>=5)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_cpKeyTyped

    private void t_municipioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_municipioKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_municipio.getText().length()>=200)
        evt.consume();
    }//GEN-LAST:event_t_municipioKeyTyped

    private void t_coloniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_coloniaKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_colonia.getText().length()>=150)
        evt.consume();
    }//GEN-LAST:event_t_coloniaKeyTyped

    private void t_numeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_numeroKeyTyped
        // TODO add your handling code here:
        if(t_numero.getText().length()>=10)
            evt.consume();
    }//GEN-LAST:event_t_numeroKeyTyped

    private void t_contactoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_contactoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_contacto.getText().length()>=200)
            evt.consume();
    }//GEN-LAST:event_t_contactoKeyTyped

    private void t_mailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_mailKeyTyped
        // TODO add your handling code here:
        if(t_mail.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_mailKeyTyped

    private void t_requestorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_requestorKeyTyped
        // TODO add your handling code here:
        if(t_requestor.getText().length()>=45)
            evt.consume();
    }//GEN-LAST:event_t_requestorKeyTyped

    private void t_rfc_emisorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_rfc_emisorKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_rfc_emisor.getText().length()>=13)
            evt.consume();
    }//GEN-LAST:event_t_rfc_emisorKeyTyped

    private void t_usuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_usuarioKeyTyped
        // TODO add your handling code here:
        if(t_usuario.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_usuarioKeyTyped

    private void t_sucursalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_sucursalKeyTyped
        // TODO add your handling code here:
        if(t_sucursal.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_sucursalKeyTyped

    private void t_telKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_telKeyTyped
        // TODO add your handling code here:
        if(t_tel.getText().length()>=13)
            evt.consume();
    }//GEN-LAST:event_t_telKeyTyped

    private void t_usuario_finkokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_usuario_finkokKeyTyped
        // TODO add your handling code here:
        if(t_usuario_finkok.getText().length()>=45)
            evt.consume();
    }//GEN-LAST:event_t_usuario_finkokKeyTyped

    private void t_clave_finkokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_clave_finkokKeyTyped
        // TODO add your handling code here:
        if(t_clave_finkok.getText().length()>=12)
            evt.consume();
    }//GEN-LAST:event_t_clave_finkokKeyTyped

    private void rb_finkokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_finkokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_finkokActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try
        {
            selector=new JFileChooser();
            selector.setFileFilter(new ExtensionFileFilter("archivo CER", new String[] { "CER" }));
            selector.setAcceptAllFileFilterUsed(false);
            selector.setMultiSelectionEnabled(false);
            File destino=null;
            int estado=selector.showOpenDialog(null);
            File archivo = selector.getSelectedFile();
            if(estado==0)
            {
                if(archivo.exists())
                {   
                    File folder = new File(ruta+"config/");
                    folder.mkdirs();
                    destino = new File(ruta+"config/"+archivo.getName());
                    InputStream in = new FileInputStream(archivo);
                    OutputStream out = new FileOutputStream(destino);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) 
                        out.write(buf, 0, len);
                    in.close();
                    out.close();
                    t_cer.setText(archivo.getName());
                }
            }
        }catch (Exception ioe)
        {
            ioe.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error no se pudo cargar el archivo");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try
        {
            selector=new JFileChooser();
            selector.setFileFilter(new ExtensionFileFilter("archivo KEY", new String[] { "KEY" }));
            selector.setAcceptAllFileFilterUsed(false);
            selector.setMultiSelectionEnabled(false);
            File destino=null;
            int estado=selector.showOpenDialog(null);
            File archivo = selector.getSelectedFile();
            if(estado==0)
            {
                if(archivo.exists())
                {   
                    File folder = new File(ruta+"config/");
                    folder.mkdirs();
                    destino = new File(ruta+"config/"+archivo.getName());
                    InputStream in = new FileInputStream(archivo);
                    OutputStream out = new FileOutputStream(destino);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) 
                        out.write(buf, 0, len);
                    in.close();
                    out.close();
                    t_key.setText(archivo.getName());
                }
            }
        }catch (Exception ioe)
        {
            ioe.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error no se pudo cargar el archivo");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void c_empresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_empresaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_c_empresaItemStateChanged

    private void c_empresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_empresaActionPerformed
        // TODO add your handling code here:
        busca(c_empresa.getSelectedIndex()+1);
    }//GEN-LAST:event_c_empresaActionPerformed

    private void t_serieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_serieKeyTyped
        // TODO add your handling code here:
        if(t_serie.getText().length()>=3)
            evt.consume();
    }//GEN-LAST:event_t_serieKeyTyped

    private void t_serieFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_serieFocusLost
        // TODO add your handling code here:
        if(t_serie.getText().length()>3)
            t_serie.setText(t_serie.getText().substring(0,3));
    }//GEN-LAST:event_t_serieFocusLost

    private void t_serie1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_serie1FocusLost
        // TODO add your handling code here:
        if(t_serie1.getText().length()>3)
            t_serie1.setText(t_serie1.getText().substring(0,3));
    }//GEN-LAST:event_t_serie1FocusLost

    private void t_serie1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_serie1KeyTyped
        // TODO add your handling code here:
        if(t_serie1.getText().length()>=3)
            evt.consume();
    }//GEN-LAST:event_t_serie1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox c_empresa;
    private javax.swing.JComboBox c_estado;
    private javax.swing.JComboBox c_pais;
    private javax.swing.ButtonGroup grupo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel l_cer;
    private javax.swing.JLabel l_key;
    private javax.swing.JLabel l_key1;
    private javax.swing.JLabel l_key2;
    private javax.swing.JPanel p_cinta;
    private javax.swing.JPanel p_factura;
    private javax.swing.JPanel p_logo;
    private javax.swing.JPanel p_sello;
    private javax.swing.JTabbedPane pestana;
    private javax.swing.JRadioButton rb_finkok;
    private javax.swing.JRadioButton rb_mysuite;
    private javax.swing.JTextField t_cer;
    private javax.swing.JTextField t_clave;
    private javax.swing.JTextField t_clave_finkok;
    private javax.swing.JTextField t_colonia;
    private javax.swing.JTextField t_contacto;
    private javax.swing.JTextField t_cp;
    private javax.swing.JTextField t_direccion;
    private javax.swing.JTextField t_iva;
    private javax.swing.JTextField t_key;
    private javax.swing.JTextField t_mail;
    private javax.swing.JTextField t_municipio;
    private javax.swing.JTextField t_numero;
    private javax.swing.JTextField t_razon_social;
    private javax.swing.JTextField t_requestor;
    private javax.swing.JTextField t_rfc_emisor;
    private javax.swing.JTextField t_serie;
    private javax.swing.JTextField t_serie1;
    private javax.swing.JTextField t_sucursal;
    private javax.swing.JTextField t_tel;
    private javax.swing.JTextField t_usuario;
    private javax.swing.JTextField t_usuario_finkok;
    // End of variables declaration//GEN-END:variables
}
