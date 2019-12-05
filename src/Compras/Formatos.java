/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Adicionales;
import Hibernate.entidades.ClaveUnidad;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.DocumentoPago;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Nota;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.Pago;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Relacion;
import Hibernate.entidades.Usuario;
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
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import Integral.Herramientas;
import Integral.PDF;
import Integral.numeroLetra;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import java.io.IOException;
import javax.imageio.ImageIO; 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.EnumMap;
/**
 *
 * @author salvador
 */
public class Formatos {
    
    public static final String[] ITEMS = {
        "Insurance system", "Agent", "Agency", "Agent Enrollment", "Agent Settings",
        "Appointment", "Continuing Education", "Hierarchy", "Recruiting", "Contract",
        "Message", "Correspondence", "Licensing", "Party"
    };
    private Session session;
    Herramientas h;
    String sessionPrograma="";
    Usuario usr;
    public Orden ord;
    public Factura factura=null;
    public Nota nota=null;
    public Pago pago=null;
    String no_ped="";
    int configuracion=1;
    public Formatos(Usuario u, String ses, Orden o, String p, int configuracion)
    {
        sessionPrograma=ses;
        this.configuracion=configuracion;
        usr=u;
        ord=o;
        no_ped=p;
    }
    
    public Formatos(Usuario u, String ses, Orden o, int configuracion)
    {
        this.configuracion=configuracion;
        sessionPrograma=ses;
        usr=u;
        ord=o;
    }
    
    public Formatos(Usuario u, String ses, Factura f, int configuracion)
    {
        this.configuracion=configuracion;
        sessionPrograma=ses;
        usr=u;
        factura=f;
    }
    
    public Formatos(Usuario u, String ses, Nota f, int configuracion)
    {
        this.configuracion=configuracion;
        sessionPrograma=ses;
        usr=u;
        nota=f;
    }
    
    public Formatos(Usuario u, String ses, Pago p, int configuracion)
    {
        this.configuracion=configuracion;
        sessionPrograma=ses;
        usr=u;
        pago=p;
    }
    public void factura()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        factura=(Factura)session.get(Factura.class, factura.getIdFactura());
        
        String ruta="";
        try
        {
            ruta="";
            FileReader f = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(f);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File(ruta+"xml-timbrados/");
            folder.mkdirs();
            String fi="xml-timbrados/"+factura.getRfcEmisor()+"_"+factura.getSerieExterno()+"_"+factura.getFolioExterno()+"_"+factura.getRfcReceptor()+".pdf";
            reporte.Abrir(PageSize.LETTER, "factura", "xml-timbrados/"+factura.getRfcEmisor()+"_"+factura.getSerieExterno()+"_"+factura.getFolioExterno()+"_"+factura.getRfcReceptor()+".pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{40,40,350,70,70};
            float tam_r[]=new float[]{100,100,100,100,100};
            PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);

            this.cabeceraFac(reporte, bf, tabla, factura);

            Concepto[] concepto = (Concepto[])session.createCriteria(Concepto.class).add(Restrictions.eq("factura.idFactura", factura.getIdFactura())).addOrder(Order.asc("idConcepto")).list().toArray(new Concepto[0]);

            int ren=0;
            
            BigDecimal big_total_bruto= new BigDecimal("0.0");
            BigDecimal big_sub_total=new BigDecimal("0.0");
            BigDecimal big_iva_total=new BigDecimal("0.0");
            BigDecimal big_suma_descuento=new BigDecimal("0.0");
            if(concepto.length>0)
            {
                for(int i=0; i<concepto.length; i++)
                {
                    BigDecimal big_descuento=new BigDecimal(concepto[i].getDescuento()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal big_porciento_dectuento=big_descuento.divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //cantidades de lista
                    BigDecimal big_cantidad=new BigDecimal(concepto[i].getCantidad()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal big_precio_lista=new BigDecimal(concepto[i].getPrecio()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal big_total_lista=(big_precio_lista.multiply(big_cantidad)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    big_total_bruto = big_total_bruto.add(big_total_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                    BigDecimal big_total_descuento=(big_precio_lista.multiply(big_porciento_dectuento)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //cantidades netas
                    BigDecimal big_general_descuento=(big_cantidad.multiply(big_total_descuento)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal big_precio_neto=(big_precio_lista.subtract(big_total_descuento)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    System.out.println("neto:"+big_precio_neto+"cantidad:"+big_cantidad);
                    BigDecimal big_total_neto=(big_total_lista.subtract(big_general_descuento)).setScale(2, BigDecimal.ROUND_HALF_UP);//(big_precio_neto.multiply(big_cantidad)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    big_sub_total = big_sub_total.add(big_total_neto);
                    big_suma_descuento=big_suma_descuento.add(big_general_descuento.setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    BigDecimal iva=new BigDecimal(concepto[i].getIva());
                    BigDecimal porc=iva.divide(new BigDecimal("100.0")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal monto=big_total_neto.multiply(porc).setScale(2, BigDecimal.ROUND_HALF_UP);
                    big_iva_total=big_iva_total.add(monto.setScale(2, BigDecimal.ROUND_HALF_UP));
                            
                    tabla.addCell(reporte.celda(""+concepto[i].getCantidad(), font, contenido, derecha, 0,1,12));
                    if(factura.getVersion()>3.2)
                    {
                        ClaveUnidad cve_unidad=(ClaveUnidad)session.createCriteria(ClaveUnidad.class).add(Restrictions.eq("simbolo", concepto[i].getMedida().trim())).uniqueResult();
                        tabla.addCell(reporte.celda(cve_unidad.getIdClaveUnidad(), font, contenido, izquierda, 0,1,12));
                    }
                    else
                        tabla.addCell(reporte.celda(concepto[i].getMedida(), font, contenido, izquierda, 0,1,12));
                    if(factura.getVersion()>3.2)
                    {
                        String descripcion=concepto[i].getDescripcion().toUpperCase();
                        descripcion=descripcion.replaceAll(";", "\n");
                        tabla.addCell(reporte.celda("["+concepto[i].getProductoServicio().getIdProductoServicio()+"]"+descripcion, font, contenido, izquierda, 0,1,12));
                    }
                    else
                        tabla.addCell(reporte.celda(concepto[i].getDescripcion().toUpperCase(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(big_precio_lista.doubleValue()), font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(big_total_lista), font, contenido, derecha, 0,1,12));
                }
            }
            BigDecimal big_neto = big_sub_total.add(big_iva_total);
            
            PdfPTable tabla1=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            tabla1.addCell(reporte.celda("Metodo de Pago:"+factura.getMetodoPago(), font, contenido, izquierda, 3,1,Rectangle.TOP));
            tabla1.addCell(reporte.celda("TOTAL BRUTO:", font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(big_total_bruto.doubleValue()), font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda("Lugar de Expedición: "+factura.getMunicipioEmisor()+", "+factura.getEstadoEmisor(), font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("DESCUENTO(S):", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            BigDecimal dd=new BigDecimal("0.0");
            BigDecimal neto2 = new BigDecimal("0.0");
            if(factura.getDescDeducible()!=null)
                dd=new BigDecimal(factura.getDescDeducible());
            neto2=big_neto.subtract(dd);
            
            //Configuracion con=(Configuracion)session.get(Configuracion.class, 1);
            
            //******CFDI Relacionados
            
            PdfPTable tabla0=reporte.crearTabla(5, tam_r, 100, Element.ALIGN_LEFT);
            Relacion [] relaciones=(Relacion[])factura.getRelacionsForIdFactura().toArray(new Relacion[0]);
            if(relaciones.length>0)
            {
                tabla0.addCell(reporte.celda(" ", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                PdfPCell celda1=reporte.celda("DOCUMENTOS RELACIONADOS", font, contenido, izquierda, 3,1,Rectangle.TOP+Rectangle.BOTTOM+12);
                celda1.setBackgroundColor(new GrayColor(0.9f));
                tabla0.addCell(celda1);
                
                PdfPCell celda2=reporte.celda("Tipo de Relacion: "+factura.getTipoRelacion(), font, contenido, izquierda, 2,1,Rectangle.TOP+Rectangle.BOTTOM+12);
                celda2.setBackgroundColor(new GrayColor(0.9f));
                tabla0.addCell(celda2);
                for(int r=0; r<relaciones.length; r++)
                {
                    if(relaciones[r].getFacturaByRelacionFactura()!=null)
                        tabla0.addCell(reporte.celda(relaciones[r].getFacturaByRelacionFactura().getFFiscal(), font, contenido, izquierda, 5,1,Rectangle.LEFT+Rectangle.RIGHT));
                    if(relaciones[r].getNotaByRelacionNota()!=null)
                        tabla0.addCell(reporte.celda(relaciones[r].getNotaByRelacionNota().getFFiscal(), font, contenido, izquierda, 5,1,Rectangle.LEFT+Rectangle.RIGHT));
                }
                tabla0.addCell(reporte.celda(" ", font, contenido, izquierda, 5,1,Rectangle.TOP));
            }
            
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(big_suma_descuento.doubleValue()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("(CANTIDAD CON LETRA)", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("SUBTOTAL:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));            
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(big_sub_total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(numeroLetra.convertNumberToLetter(formatoPorcentaje.format(neto2.doubleValue()))+" M.N.", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("IMPUESTOS:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(big_iva_total.doubleValue()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            switch(factura.getCondicionesPago())
                {
                    case "PAGO EN UNA SOLA EXHIBICION":
                        tabla1.addCell(reporte.celda("PAGO EN UNA SOLA EXHIBICION (PUE)", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                    case "PAGO EN PARCIALIDADES O DIFERIDO":
                        tabla1.addCell(reporte.celda("PAGO EN PARCIALIDADES O DIFERIDO (PPD)", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;    
                }
            
            tabla1.addCell(reporte.celda("TOTAL:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(big_neto.doubleValue()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            //Deducible
            tabla1.addCell(reporte.celda("", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("DEDUCIBLE:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(dd), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("TOTAL NETO:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(neto2), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            
            tabla1.addCell(reporte.celda("", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("EFECTOS FISCALES AL PAGO", font, contenido, centro, 2,1,Rectangle.NO_BORDER));
            session.beginTransaction().rollback();

            tabla.setHeaderRows(2);
            reporte.agregaObjeto(tabla);
            float tam1[]=new float[]{190,180,180,170};
            PdfPTable tabla2=reporte.crearTabla(4, tam1, 100, Element.ALIGN_LEFT);
            //Codigo RQ********
            //BufferedImage bufferedImage=createQR("?re="+factura.getRfcEmisor()+"&rr="+factura.getRfcReceptor()+"&tt="+formatoPorcentaje.format(neto)+"0000&id="+factura.getFFiscal());
            String sello8=factura.getSelloCfdi();
            BufferedImage bufferedImage=createQR("https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?id="+factura.getFFiscal()+"&re="+factura.getRfcEmisor()+"&rr="+factura.getRfcReceptor()+"&tt="+formatoPorcentaje.format(neto2.doubleValue())+"0000&fe="+sello8.substring(sello8.length()-8, sello8.length()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image iTextImage = Image.getInstance(baos.toByteArray());
            tabla2.addCell(reporte.celda(iTextImage, contenido, centro, 0,9,Rectangle.NO_BORDER));
            //******************
            if(factura.getRfcEmisor().compareToIgnoreCase("PEVV740319ML1")==0)
                tabla2.addCell(reporte.celda("Regimen Fiscal:PERSONAS FISICAS CON ACTIVIDAD EMPRESARIAL Y PROFECIONAL", font, contenido, centro, 3,1,Rectangle.BOTTOM));
            else
                tabla2.addCell(reporte.celda("Regimen Fiscal:REGIMEN GENERAL DE LEY DE PERSONAS MORALES", font, contenido, centro, 3,1,Rectangle.BOTTOM));
            tabla2.addCell(reporte.celda("NÚMERO DE SERIE DEL CERTIFICADO DEL SAT:"+factura.getCertificadoSat(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("NÚMERO DE SERIE DEL CSD DEL EMISOR:"+factura.getCertificadoEmisor(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("SELLO DIGITAL DEL SAT:", font, contenido, izquierda, 3,1,12));
            if(factura.getSelloSat()!=null)
                tabla2.addCell(reporte.celda(factura.getSelloSat(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("SELLO DIGITAL DEL CFDI:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(factura.getSelloCfdi(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("CADENA ORIGINAL DEL COMPLEMENTO DE CERTIFICACIÓN DIGITAL DEL SAT:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("||1.0|"+factura.getFFiscal()+"|"+factura.getFechaFiscal().substring(0, 19)+"|"+factura.getSelloCfdi()+"|"+factura.getCertificadoSat()+"||", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("Este documento es una representación impresa de un CFDI Ver["+factura.getVersion()+"]", font, contenido, izquierda, 2,1,Rectangle.TOP));
            switch(factura.getPac()){
                case "F":
                    tabla2.addCell(reporte.celda("CFDI emitido por Finkok S.A. de C.V. Proveedor Autorizado de Certificación (PAC)10852", font, contenido, izquierda, 2,1,Rectangle.TOP));
                    break;
                case "M":
                    tabla2.addCell(reporte.celda("CFDI emitido por MySuite Services S.A. de C.V. Proveedor. Autorizado. de Cert.(55270)", font, contenido, izquierda, 2,1,Rectangle.TOP));
                    break;
                case "T":
                    tabla2.addCell(reporte.celda("CFDI emitido por Qrea-t Solutions, S.A. de C.V. Proveedor Autorizado de Certificación (PAC)0206", font, contenido, izquierda, 2,1,Rectangle.TOP));
                    break;
            }
            tabla2.setKeepTogether(true);
            reporte.agregaObjeto(tabla1);
            if(relaciones.length>0)
                reporte.agregaObjeto(tabla0);
            reporte.agregaObjeto(tabla2);
            reporte.cerrar();
            reporte.visualizar(fi);

        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }
    
    public void pago()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        pago=(Pago)session.get(Pago.class, pago.getIdPago());
        
        String ruta="";
        try
        {
            ruta="";
            FileReader f = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(f);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            String total="";
            File folder = new File(ruta+"xml-timbrados/");
            folder.mkdirs();
            String fi="xml-timbrados/"+pago.getRfcEmisor()+"_"+pago.getSerie()+"_"+pago.getFolio()+"_"+pago.getRfcReceptor()+"-p.pdf";
            reporte.Abrir(PageSize.LETTER, "pago", "xml-timbrados/"+pago.getRfcEmisor()+"_"+pago.getSerie()+"_"+pago.getFolio()+"_"+pago.getRfcReceptor()+"-p.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{40,40,350,70,70};
            float tam_r[]=new float[]{100,100,100,100,100};
            float tam_c[]=new float[]{250,50,50,50,50,50};
            PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            this.cabeceraPag(reporte, bf, tabla, pago);
            
            PdfPTable tabla1=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            tabla1.addCell(reporte.celda("Lugar de Expedición: "+pago.getMunicipioEmisor()+", "+pago.getEstadoEmisor(), font, contenido, izquierda, 4,1,Rectangle.TOP));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(0.0d), font, contenido, derecha, 0,1,Rectangle.RECTANGLE+Rectangle.TOP));
            
            tabla1.addCell(reporte.celda("", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
            
            //Documentos Pagados
            PdfPTable tabla3=reporte.crearTabla(6, tam_c, 100, Element.ALIGN_LEFT);
            DocumentoPago[] concepto = (DocumentoPago[])session.createCriteria(DocumentoPago.class).
                    add(Restrictions.eq("pago.idPago", pago.getIdPago())).
                    addOrder(Order.asc("idDocumentoPago")).list().toArray(new DocumentoPago[0]);
            double neto=0.0d;
            for(int r=0; r<concepto.length; r++)
            {
                neto=neto+concepto[r].getImporte();
            }
            
            PdfPCell celda_1=reporte.celda("CFDI PAGOS", font, contenido, izquierda, 6,1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda_1.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_1);
            tabla3.addCell(reporte.celda("", font, contenido, derecha, 6,1,Rectangle.BOTTOM));
            
            tabla3.addCell(reporte.celda("Fecha Pago:"+pago.getFechaPago(), font, contenido, izquierda, 5,1,Rectangle.LEFT));
            tabla3.addCell(reporte.celda("Total Pago", font, contenido, izquierda, 0,1,Rectangle.RIGHT));
            
            if(pago.getReferencia()!=null)
                tabla3.addCell(reporte.celda("Referencia: "+pago.getReferencia(), font, contenido, izquierda, 1,1,Rectangle.LEFT));
            tabla3.addCell(reporte.celda(" ", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
            tabla3.addCell(reporte.celda(formatoPorcentaje.format(neto), font, contenido, izquierda, 0,1,Rectangle.RIGHT));
            
            tabla3.addCell(reporte.celda(numeroLetra.convertNumberToLetter(formatoPorcentaje.format(neto))+" M.N.", font, contenido, izquierda, 6,1,Rectangle.LEFT+Rectangle.RIGHT+Rectangle.BOTTOM));
            
            PdfPCell celda_2=reporte.celda("UUID", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE);
            celda_2.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_2);
            
            PdfPCell celda_3=reporte.celda("Factura", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE);
            celda_3.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_3);
            
            PdfPCell celda_4=reporte.celda("Parcialidad", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE);
            celda_4.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_4);
            
            PdfPCell celda_5=reporte.celda("Sal.Ant.", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE);
            celda_5.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_5);
            
            PdfPCell celda_6=reporte.celda("Imp.Pagado", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE);
            celda_6.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_6);
            
            PdfPCell celda_7=reporte.celda("Sal.Insol.", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE);
            celda_7.setBackgroundColor(new GrayColor(0.9f));
            tabla3.addCell(celda_7);
            
            for(int r=0; r<concepto.length; r++)
            {
                tabla3.addCell(reporte.celda(concepto[r].getFactura().getFFiscal(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                String serie="";
                if(concepto[r].getFactura().getVersion()>3.2)
                {
                    if(concepto[r].getFactura().getSerieExterno()!=null)
                        serie=concepto[r].getFactura().getSerieExterno();
                    tabla3.addCell(reporte.celda(serie+concepto[r].getFactura().getFolioExterno(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                }
                else
                {
                    if(concepto[r].getFactura().getSerie()!=null)
                        serie=concepto[r].getFactura().getSerie();
                    tabla3.addCell(reporte.celda(serie+concepto[r].getFactura().getFolio(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                }
                tabla3.addCell(reporte.celda(""+concepto[r].getParcialidad(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla3.addCell(reporte.celda(formatoPorcentaje.format(concepto[r].getSaldoAnterior()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla3.addCell(reporte.celda(formatoPorcentaje.format(concepto[r].getImporte()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla3.addCell(reporte.celda(formatoPorcentaje.format(concepto[r].getSaldoInsoluto()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
            }
            tabla3.addCell(reporte.celda(" ", font, contenido, izquierda, 6,1,Rectangle.TOP));
            
            //******CFDI Relacionados
            PdfPTable tabla0=reporte.crearTabla(5, tam_r, 100, Element.ALIGN_LEFT);
            Relacion [] relaciones=(Relacion[])pago.getRelacionsForIdPago().toArray(new Relacion[0]);
            if(relaciones.length>0)
            {
                tabla0.addCell(reporte.celda(" ", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                PdfPCell celda1=reporte.celda("DOCUMENTOS RELACIONADOS", font, contenido, izquierda, 3,1,Rectangle.TOP+Rectangle.BOTTOM+12);
                celda1.setBackgroundColor(new GrayColor(0.9f));
                tabla0.addCell(celda1);
                
                PdfPCell celda2=reporte.celda("Tipo de Relacion: "+pago.getTipoRelacion(), font, contenido, izquierda, 2,1,Rectangle.TOP+Rectangle.BOTTOM+12);
                celda2.setBackgroundColor(new GrayColor(0.9f));
                tabla0.addCell(celda2);
                for(int r=0; r<relaciones.length; r++)
                {
                    if(relaciones[r].getPagoByRelacionPago()!=null)
                        tabla0.addCell(reporte.celda(relaciones[r].getPagoByRelacionPago().getFFiscal(), font, contenido, izquierda, 5,1,Rectangle.LEFT+Rectangle.RIGHT));
                }
                tabla0.addCell(reporte.celda(" ", font, contenido, izquierda, 5,1,Rectangle.TOP));
            }
            
            session.beginTransaction().rollback();

            tabla.setHeaderRows(2);
            reporte.agregaObjeto(tabla);
            float tam1[]=new float[]{190,180,180,170};
            PdfPTable tabla2=reporte.crearTabla(4, tam1, 100, Element.ALIGN_LEFT);
            //Codigo RQ********
            BigDecimal numero= new BigDecimal(neto);
            String sello8=pago.getSelloCfdi();
            BufferedImage bufferedImage=createQR("https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?id="+pago.getFFiscal()+"&re="+pago.getRfcEmisor()+"&rr="+pago.getRfcReceptor()+"&tt=0.00&fe="+sello8.substring(sello8.length()-8, sello8.length()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image iTextImage = Image.getInstance(baos.toByteArray());
            tabla2.addCell(reporte.celda(iTextImage, contenido, centro, 0,9,Rectangle.RECTANGLE+Rectangle.TOP));
            //******************
            if(pago.getRfcEmisor().compareToIgnoreCase("PEVV740319ML1")==0)
                tabla2.addCell(reporte.celda("Regimen Fiscal:PERSONAS FISICAS CON ACTIVIDAD EMPRESARIAL Y PROFECIONAL", font, contenido, centro, 3,1,Rectangle.BOTTOM));
            else
                tabla2.addCell(reporte.celda("Regimen Fiscal:REGIMEN GENERAL DE LEY DE PERSONAS MORALES", font, contenido, centro, 3,1,Rectangle.BOTTOM));
            tabla2.addCell(reporte.celda("NÚMERO DE SERIE DEL CERTIFICADO DEL SAT:"+pago.getCertificadoSat(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("NÚMERO DE SERIE DEL CSD DEL EMISOR:"+pago.getCertificadoEmisor(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("SELLO DIGITAL DEL SAT:", font, contenido, izquierda, 3,1,12));
            if(pago.getSelloSat()!=null)
                tabla2.addCell(reporte.celda(pago.getSelloSat(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("SELLO DIGITAL DEL CFDI:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(pago.getSelloCfdi(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("CADENA ORIGINAL DEL COMPLEMENTO DE CERTIFICACIÓN DIGITAL DEL SAT:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("||1.0|"+pago.getFFiscal()+"|"+pago.getFechaFiscal().substring(0, 19)+"|"+pago.getSelloCfdi()+"|"+pago.getCertificadoSat()+"||", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("Este documento es una representación impresa de un CFDI Ver["+pago.getVersion()+"]", font, contenido, izquierda, 2,1,Rectangle.TOP));
            tabla2.addCell(reporte.celda("CFDI emitido por Qrea-t Solutions, S.A. de C.V. Proveedor Autorizado de Certificación (PAC)0206", font, contenido, izquierda, 2,1,Rectangle.TOP));
            tabla2.setKeepTogether(true);
            reporte.agregaObjeto(tabla1);
            reporte.agregaObjeto(tabla3);
            if(relaciones.length>0)
                reporte.agregaObjeto(tabla0);
            reporte.agregaObjeto(tabla2);
            reporte.cerrar();
            reporte.visualizar(fi);

        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }
    
    public void nota()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        nota=(Nota)session.get(Nota.class, nota.getIdNota());
        
        String ruta="";
        try
        {
            ruta="";
            FileReader f = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(f);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File(ruta+"xml-timbrados/");
            folder.mkdirs();
            String fi="xml-timbrados/"+nota.getRfcEmisor()+"_"+nota.getSerieExterno()+"_"+nota.getFolioExterno()+"_"+nota.getRfcReceptor()+".pdf";
            reporte.Abrir(PageSize.LETTER, "Pedido", "xml-timbrados/"+nota.getRfcEmisor()+"_"+nota.getSerieExterno()+"_"+nota.getFolioExterno()+"_"+nota.getRfcReceptor()+".pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{40,40,350,70,70};
            float tam_r[]=new float[]{100,100,100,100,100};
            PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);

            this.cabeceraNot(reporte, bf, tabla, nota);

            Concepto[] concepto = (Concepto[])session.createCriteria(Concepto.class).add(Restrictions.eq("nota.idNota", nota.getIdNota())).addOrder(Order.asc("idConcepto")).list().toArray(new Concepto[0]);

            int ren=0;
            double total=0.0;
            double descuento=0.0d;
            if(concepto.length>0)
            {
                for(int i=0; i<concepto.length; i++)
                {
                    tabla.addCell(reporte.celda(""+concepto[i].getCantidad(), font, contenido, derecha, 0,1,12));
                    if(nota.getVersion()>3.2)
                    {
                        ClaveUnidad cve_unidad=(ClaveUnidad)session.createCriteria(ClaveUnidad.class).add(Restrictions.eq("simbolo", concepto[i].getMedida().trim())).uniqueResult();
                        tabla.addCell(reporte.celda(cve_unidad.getIdClaveUnidad(), font, contenido, izquierda, 0,1,12));
                    }
                    else
                        tabla.addCell(reporte.celda(concepto[i].getMedida(), font, contenido, izquierda, 0,1,12));
                    
                    if(nota.getVersion()>3.2)
                        tabla.addCell(reporte.celda(concepto[i].getDescripcion().toUpperCase()+"["+concepto[i].getProductoServicio().getIdProductoServicio()+"]", font, contenido, izquierda, 0,1,12));
                    else
                        tabla.addCell(reporte.celda(concepto[i].getDescripcion().toUpperCase(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(concepto[i].getPrecio()), font, contenido, derecha, 0,1,12));
                    double tot=concepto[i].getPrecio()*concepto[i].getCantidad();
                    total+=tot;
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,12));
                    if(concepto[i].getDescuento()>0)
                    {
                        double desc=concepto[i].getDescuento()/100;
                        descuento+=tot*desc;
                    }
                }
            }
            PdfPTable tabla1=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            tabla1.addCell(reporte.celda("Metodo de Pago:"+nota.getMetodoPago(), font, contenido, izquierda, 3,1,Rectangle.TOP));
            tabla1.addCell(reporte.celda("SUBTOTAL BRUTO:", font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda("Lugar de Expedición: "+nota.getMunicipioEmisor()+", "+nota.getEstadoEmisor(), font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("DESCUENTO(S):", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            //Configuracion con=(Configuracion)session.get(Configuracion.class, configuracion);
            
            //******CFDI Relacionados
            
            PdfPTable tabla0=reporte.crearTabla(5, tam_r, 100, Element.ALIGN_LEFT);
            Relacion [] relaciones=(Relacion[])nota.getRelacionsForIdNota().toArray(new Relacion[0]);
            if(relaciones.length>0)
            {
                tabla0.addCell(reporte.celda(" ", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                PdfPCell celda1=reporte.celda("DOCUMENTOS RELACIONADOS", font, contenido, izquierda, 3,1,Rectangle.TOP+Rectangle.BOTTOM+12);
                celda1.setBackgroundColor(new GrayColor(0.9f));
                tabla0.addCell(celda1);
                
                PdfPCell celda2=reporte.celda("Tipo de Relacion: "+nota.getTipoRelacion(), font, contenido, izquierda, 2,1,Rectangle.TOP+Rectangle.BOTTOM+12);
                celda2.setBackgroundColor(new GrayColor(0.9f));
                tabla0.addCell(celda2);
                for(int r=0; r<relaciones.length; r++)
                {
                    if(relaciones[r].getFacturaByRelacionFactura()!=null)
                        tabla0.addCell(reporte.celda(relaciones[r].getFacturaByRelacionFactura().getFFiscal(), font, contenido, izquierda, 5,1,Rectangle.LEFT+Rectangle.RIGHT));
                    if(relaciones[r].getNotaByRelacionNota()!=null)
                        tabla0.addCell(reporte.celda(relaciones[r].getNotaByRelacionNota().getFFiscal(), font, contenido, izquierda, 5,1,Rectangle.LEFT+Rectangle.RIGHT));
                }
                tabla0.addCell(reporte.celda(" ", font, contenido, izquierda, 5,1,Rectangle.TOP));
            }
            
            double sub=total-descuento;
            double iva=sub*(nota.getIva()*0.01);
            double neto=sub+iva;
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(descuento), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("(CANTIDAD CON LETRA)", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("SUBTOTAL:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));            
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(sub), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(numeroLetra.convertNumberToLetter(formatoPorcentaje.format(neto))+" M.N.", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("IMPUESTOS:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            switch(nota.getCondicionesPago())
            {
                case "PAGO EN UNA SOLA EXHIBICION":
                    tabla1.addCell(reporte.celda("PAGO EN UNA SOLA EXHIBICION (PUE)", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                    break;
                case "PAGO EN PARCIALIDADES O DIFERIDO":
                    tabla1.addCell(reporte.celda("PAGO EN PARCIALIDADES O DIFERIDO (PPD)", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                    break;    
            }
            tabla1.addCell(reporte.celda("TOTAL", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(neto), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            
            tabla1.addCell(reporte.celda("", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("EFECTOS FISCALES AL PAGO", font, contenido, centro, 2,1,Rectangle.NO_BORDER));
            session.beginTransaction().rollback();
           
            tabla.setHeaderRows(2);
            reporte.agregaObjeto(tabla);
            float tam1[]=new float[]{190,180,180,170};
            PdfPTable tabla2=reporte.crearTabla(4, tam1, 100, Element.ALIGN_LEFT);
            //Codigo RQ********
            BigDecimal numero= new BigDecimal(neto);
            //BufferedImage bufferedImage=createQR("?re="+nota.getRfcEmisor()+"&rr="+nota.getRfcReceptor()+"&tt="+formatoPorcentaje.format(neto)+"0000&id="+nota.getFFiscal());
            String sello8=nota.getSelloCfdi();
            BufferedImage bufferedImage=createQR("https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?id="+nota.getFFiscal()+"&re="+nota.getRfcEmisor()+"&rr="+nota.getRfcReceptor()+"&tt="+formatoPorcentaje.format(neto)+"0000&fe="+sello8.substring(sello8.length()-8, sello8.length()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image iTextImage = Image.getInstance(baos.toByteArray());
            tabla2.addCell(reporte.celda(iTextImage, contenido, centro, 0,9,Rectangle.NO_BORDER));
            //******************
            tabla2.addCell(reporte.celda("Regimen Fiscal:REGIMEN GENERAL DE LEY DE PERSONAS MORALES", font, contenido, centro, 3,1,Rectangle.BOTTOM));
            tabla2.addCell(reporte.celda("NÚMERO DE SERIE DEL CERTIFICADO DEL SAT:"+nota.getCertificadoSat(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("NÚMERO DE SERIE DEL CSD DEL EMISOR:"+nota.getCertificadoEmisor(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("SELLO DIGITAL DEL SAT:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(nota.getSelloSat(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("SELLO DIGITAL DEL CFDI:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(nota.getSelloCfdi(), font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("CADENA ORIGINAL DEL COMPLEMENTO DE CERTIFICACIÓN DIGITAL DEL SAT:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("||1.0|"+nota.getFFiscal()+"|"+nota.getFFiscal()+"|"+nota.getSelloCfdi()+"|"+nota.getCertificadoSat()+"||", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("Este documento es una representación impresa de un CFDI Ver["+nota.getVersion()+"]", font, contenido, izquierda, 2,1,Rectangle.TOP));
            switch(nota.getPac()){
                case "F":
                    tabla2.addCell(reporte.celda("CFDI emitido por Finkok S.A. de C.V. Proveedor Autorizado de Certificación (PAC)10852", font, contenido, izquierda, 2,1,Rectangle.TOP));
                    break;
                case "M":
                    tabla2.addCell(reporte.celda("CFDI emitido por MySuite Services S.A. de C.V. Proveedor. Autorizado. de Cert.(55270)", font, contenido, izquierda, 2,1,Rectangle.TOP));
                    break;
                case "T":
                    tabla2.addCell(reporte.celda("CFDI emitido por Qrea-t Solutions, S.A. de C.V. Proveedor Autorizado de Certificación (PAC)0206", font, contenido, izquierda, 2,1,Rectangle.TOP));
                    break;
            }
            
            tabla2.setKeepTogether(true);
            reporte.agregaObjeto(tabla1);
            if(relaciones.length>0)
                reporte.agregaObjeto(tabla0);
            reporte.agregaObjeto(tabla2);
            reporte.cerrar();
            reporte.visualizar(fi);

        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }
    
    public void prefactura()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        ord=(Orden)session.get(Orden.class, ord.getIdOrden());
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "Pedido", "reportes/"+ord.getIdOrden()+"/"+valor+"-preFac.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{40,40,350,70,70};
            PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            cabeceraPre(reporte, bf, tabla);
            Partida[] cuentas =(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("facturado", true)).add(Restrictions.eq("incluida", false)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
            Partida[] enlazadas =(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByEnlazada.idOrden", ord.getIdOrden())).add(Restrictions.eq("facturado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
            Adicionales[] externas = (Adicionales[])session.createCriteria(Adicionales.class).add(Restrictions.eq("orden.idOrden", ord.getIdOrden())).addOrder(Order.asc("idAdicionales")).list().toArray(new Adicionales[0]);

            //**********agregamos la mano de obra*******************
            double tot_mo=0.0d;
            Query query = session.createQuery("SELECT SUM( "
                    + "(CASE WHEN dm>0 THEN (dm*cant) ELSE 0 END) + "
                    + "(CASE WHEN cam>0 THEN (cam*cant) ELSE 0 END) + "
                    + "(CASE WHEN repMin>0 THEN (repMin*cant) ELSE 0 END) + "
                    + "(CASE WHEN repMed>0 THEN (repMed*cant) ELSE 0 END) + "
                    + "(CASE WHEN repMax>0 THEN (repMax*cant) ELSE 0 END) + "
                    + "(CASE WHEN pint>0 THEN (pint*cant) ELSE 0 END) "
                    + ") from Partida "+
                        " where ordenByIdOrden.idOrden ="+ord.getIdOrden() +
                        " OR ordenByEnlazada.idOrden ="+ord.getIdOrden());
                Object  ent = query.uniqueResult();
                if(ent!=null)
                {
                    tot_mo=Double.parseDouble(ent.toString())*ord.getCompania().getImporteHora();
                }
                if(ord.getMoDirecta()>0d)
                    tot_mo=ord.getMoDirecta();
            tabla.addCell(reporte.celda("1", font, contenido, derecha, 0,1,12));
            tabla.addCell(reporte.celda("NA", font, contenido, izquierda, 0,1,12));
            tabla.addCell(reporte.celda("MANO DE OBRA", font, contenido, izquierda, 0,1,12));
            tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot_mo), font, contenido, derecha, 0,1,12));
            tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot_mo), font, contenido, derecha, 0,1,12));
            int ren=0;
            double total=tot_mo;
            if(cuentas.length>0)
            {
                for(int i=0; i<cuentas.length; i++)
                {
                    tabla.addCell(reporte.celda(""+cuentas[i].getCantidadFactura(), font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(cuentas[i].getDescripcionFactura().toUpperCase(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(cuentas[i].getPrecioFactura()), font, contenido, derecha, 0,1,12));
                    double tot=cuentas[i].getPrecioFactura()*cuentas[i].getCantidadFactura();
                    total+=tot;
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,12));
                }
            }
            if(enlazadas.length>0)
            {
                for(int i=0; i<enlazadas.length; i++)
                {
                    tabla.addCell(reporte.celda(""+enlazadas[i].getCantidadFactura(), font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(enlazadas[i].getMed(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(enlazadas[i].getDescripcionFactura().toUpperCase(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(enlazadas[i].getPrecioFactura()), font, contenido, derecha, 0,1,12));
                    double tot=enlazadas[i].getPrecioFactura()*enlazadas[i].getCantidadFactura();
                    total+=tot;
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,12));
                }
            }
            if(externas.length>0)
            {
                for(int ex=0; ex<externas.length; ex++)
                {
                    tabla.addCell(reporte.celda(""+externas[ex].getCantidad(), font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(externas[ex].getMedida(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(externas[ex].getDescripcion().toUpperCase(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(externas[ex].getPrecio()), font, contenido, derecha, 0,1,12));
                    double tot=externas[ex].getPrecio()*externas[ex].getCantidad();
                    total+=tot;
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,12));
                }
            }
            
            PdfPTable tabla1=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            tabla1.addCell(reporte.celda("Metodo de Pago: NO IDENTIFICADO", font, contenido, izquierda, 3,1,Rectangle.TOP));
            tabla1.addCell(reporte.celda("SUB-TOTAL:", font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda("Lugar de Expedición: ", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("IVA:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            Configuracion con=(Configuracion)session.get(Configuracion.class, configuracion);
            double iva=total*(con.getIva()*0.01);
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("(CANTIDAD CON LETRA)", font, contenido, izquierda, 3,2,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("DEDUCIBLE:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("$0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("TOTAL:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            total+=iva;
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("PAGO EN UNA SOLA EXHIBICIóN", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("EFECTOS FISCALES AL PAGO", font, contenido, centro, 2,1,Rectangle.NO_BORDER));
            session.beginTransaction().rollback();

            tabla.setHeaderRows(2);
            reporte.agregaObjeto(tabla);
            float tam1[]=new float[]{180,180,180,180};
            PdfPTable tabla2=reporte.crearTabla(4, tam1, 100, Element.ALIGN_LEFT);
            tabla2.addCell(reporte.celda(reporte.Imagen("imagenes/rq.png"), contenido, centro, 0,8,Rectangle.NO_BORDER));
            tabla2.addCell(reporte.celda("Regimen Fiscal:REGIMEN GENERAL DE LEY DE PERSONAS MORALES", font, contenido, centro, 3,1,Rectangle.BOTTOM));
            tabla2.addCell(reporte.celda("Sello Digital del SAT:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(" ", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("Sello Digital del Emisor:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(" ", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("Cadena original del complemento de certificación digital del SAT:", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda(" ", font, contenido, izquierda, 3,1,12));
            tabla2.addCell(reporte.celda("Este documento es una representación impresa de un CFDI", font, contenido, izquierda, 3,1,Rectangle.TOP));
            
            reporte.agregaObjeto(tabla1);
            reporte.agregaObjeto(tabla2);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-preFac.pdf");

        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }
    
    void pedidos()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        ord=(Orden)session.get(Orden.class, ord.getIdOrden());
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
            formatoPorcentaje.setMinimumFractionDigits(2);

            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "cabecera", "reportes/"+ord.getIdOrden()+"/"+valor+"-pedido.pdf");
            
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{20,30,25,60,190,25,30,20,40,40};
            PdfPTable tabla=reporte.crearTabla(10, tam, 100, Element.ALIGN_LEFT);

            Pedido ped = (Pedido)session.get(Pedido.class, Integer.parseInt(this.no_ped));
            if(ped.getUsuarioByAutorizo()!=null && ped.getUsuarioByAutorizo2()!=null)
            {
                reporte.estatusAutoriza(ped.getEmpleado().getNombre(), ped.getUsuarioByAutorizo().getIdUsuario()+"/"+ ped.getUsuarioByAutorizo2().getIdUsuario());
                //reporte.estatusAutoriza(, ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
            }
            else
                reporte.estatusAutoriza(ped.getEmpleado().getNombre(), "  NO AUTORIZADO / NO AUTORIZADO");
            cabecera(reporte, bf, tabla, ped);

            Partida[] cuentas =(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("pedido.idPedido", Integer.parseInt(no_ped))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);

            int ren=0;
            double total=0d;
            if(cuentas.length>0)
            {
                for(int i=0; i<cuentas.length; i++)
                {
                    int r=i+1;
                    //consecutivo
                    tabla.addCell(reporte.celda(""+r, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                    //Partida y subpartida
                    tabla.addCell(reporte.celda(""+cuentas[i].getIdEvaluacion()+ " "+cuentas[i].getSubPartida(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    //folio del articulo de articulo
                    tabla.addCell(reporte.celda(""+cuentas[i].getCatalogo().getIdCatalogo(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    if(cuentas[i].getEjemplar()!=null)
                    {
                        //No de parte
                        tabla.addCell(reporte.celda(""+cuentas[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    }
                    else
                        tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    //Descripcion
                    String anotacion="";
                    if(cuentas[i].getInstruccion()!=null)
                        anotacion=cuentas[i].getInstruccion();
                    tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre()+" "+anotacion, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                    //Medida
                    tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(cuentas[i].getPlazo()!=null)//plazo de entrega
                    tabla.addCell(reporte.celda(""+cuentas[i].getPlazo(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    else
                    tabla.addCell(reporte.celda("0", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(cuentas[i].getCantPcp()>0)//cantidad a comprar
                        tabla.addCell(reporte.celda(""+cuentas[i].getCantPcp(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    else
                        tabla.addCell(reporte.celda("0", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(cuentas[i].getPcp()!=null)//costo unit
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getPcp()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    else
                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(cuentas[i].getCantPcp()>0 && cuentas[i].getPcp()!=null)//costo total
                    {
                        double sum=cuentas[i].getCantPcp() * cuentas[i].getPcp();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    }
                    else
                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    ren++;
                }
            }
            switch(ped.getFormaPago())
            {
                case 1:
                    tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Credito]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                    break;

                case 2:
                    tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Contado]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                    break;

                case 3:
                    tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Efectivo]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                    break;

                case 4:
                    tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Cheque]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                    break;
            }
            tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 4,1,Rectangle.NO_BORDER));
            tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 9,1,Rectangle.NO_BORDER));
            double iva=total*0.16d;
            tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 9,1,Rectangle.NO_BORDER));
            total+=iva;
            tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            if(ped.getNotas()!=null)
                tabla.addCell(reporte.celda("Notas: "+ped.getNotas(), font, contenido, izquierda, 10,1,Rectangle.NO_BORDER));
            tabla.setHeaderRows(1);
            session.beginTransaction().rollback();

            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-pedido.pdf");

        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }
    
    void pedidosExternos(int pedido)
    {
            h=new Herramientas(usr, 0);
            h.session(sessionPrograma);
            session = HibernateUtil.getSessionFactory().openSession();
            Pedido ped=(Pedido)session.get(Pedido.class, pedido);
            try
            {
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
                formatoPorcentaje.setMinimumFractionDigits(2);

                session.beginTransaction().begin();
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

                PDF reporte = new PDF();
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                File folder = new File("reportes/externos");
                folder.mkdirs();
                reporte.Abrir2(PageSize.LETTER, "cabecera", "reportes/externos/"+valor+"-pedido.pdf");
                Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
                BaseColor contenido=null;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;
                float tam[]=new float[]{20,30,25,60,190,25,30,20,40,40};
                PdfPTable tabla=reporte.crearTabla(10, tam, 100, Element.ALIGN_LEFT);

                if(ped.getUsuarioByAutorizo()!=null && ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(ped.getEmpleado().getNombre(), ped.getUsuarioByAutorizo().getIdUsuario()+"/"+ped.getUsuarioByAutorizo2().getIdUsuario());
                }
                else
                    reporte.estatusAutoriza("       NO AUTORIZADO","       NO AUTORIZADO");
                cabecera(reporte, bf, tabla, ped);

                PartidaExterna[] cuentas =(PartidaExterna[]) session.createCriteria(PartidaExterna.class).
                        add(Restrictions.eq("pedido.idPedido", pedido)).
                        addOrder(Order.asc("idPartidaExterna")).
                        list().toArray(new PartidaExterna[0]);

                int ren=0;
                double total=0d;
                if(cuentas.length>0)
                {
                    for(int i=0; i<cuentas.length; i++)
                    {
                        int r=i+1;
                        //consecutivo
                        tabla.addCell(reporte.celda(""+r, font, contenido, izquierda, 0,1,Rectangle.NO_BORDER));

                        //Partida y subpartida
                        if(cuentas[i].getPartida()!=null)
                            tabla.addCell(reporte.celda(""+cuentas[i].getPartida()+ " "+cuentas[i].getIdValuacion(), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                        else
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                            

                        //folio del articulo de articulo
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                        if(ped.getTipoPedido().compareTo("Inventario")==0)
                            tabla.addCell(reporte.celda(""+cuentas[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                        else
                            tabla.addCell(reporte.celda(""+cuentas[i].getNoParte(), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));

                        //Descripcion
                        tabla.addCell(reporte.celda(cuentas[i].getDescripcion(), font, contenido, izquierda, 0,1,Rectangle.NO_BORDER));

                        //Medida
                        tabla.addCell(reporte.celda(cuentas[i].getUnidad(), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));

                        if(cuentas[i].getPlazo()!=null)//plazo de entrega
                        tabla.addCell(reporte.celda(""+cuentas[i].getPlazo(), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                        else
                        tabla.addCell(reporte.celda("0", font, contenido, derecha, 0,1,Rectangle.NO_BORDER));

                        //cantidad a compra
                        tabla.addCell(reporte.celda(""+cuentas[i].getCantidad(), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                        
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCosto()), font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
                        
                        double sum=cuentas[i].getCantidad() * cuentas[i].getCosto();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.UNDEFINED));

                    }
                }
                tabla.addCell(reporte.celda("", font, contenido, derecha, 9,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.BOTTOM));
                switch(ped.getFormaPago())
                {
                    case 1:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Credito]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 2:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Contado]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                        break;
                    
                    case 3:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Efectivo]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 4:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Cheque]", font, contenido, izquierda, 5,1,Rectangle.NO_BORDER));
                        break;
                }
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 4,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 9,1,Rectangle.NO_BORDER));
                double iva=total*0.16d;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 9,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                if(ped.getNotas()!=null)
                    tabla.addCell(reporte.celda("Notas: "+ped.getNotas(), font, contenido, izquierda, 10,1,Rectangle.NO_BORDER));
                
                tabla.setHeaderRows(1);
                session.beginTransaction().rollback();

                reporte.agregaObjeto(tabla);
                
                reporte.cerrar();
                reporte.visualizar2("reportes/externos/"+valor+"-pedido.pdf");

            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
            }
    }
    
    void ordenCompra()
    {
        h=new Herramientas(usr, 0);
            h.session(sessionPrograma);
            session = HibernateUtil.getSessionFactory().openSession();
            ord=(Orden)session.get(Orden.class, ord.getIdOrden());
            try
            {
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
                formatoPorcentaje.setMinimumFractionDigits(2);

                session.beginTransaction().begin();
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

                PDF reporte = new PDF();
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                File folder = new File("reportes/"+ord.getIdOrden());
                folder.mkdirs();
                reporte.Abrir2(PageSize.LETTER, "cabecera", "reportes/"+ord.getIdOrden()+"/"+valor+"-orden.pdf");
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;
                float tam[]=new float[]{20,20,220,40,90,50,50};
                PdfPTable tabla=reporte.crearTabla(7, tam, 100, Element.ALIGN_LEFT);

                Pedido ped = (Pedido)session.get(Pedido.class, Integer.parseInt(no_ped));
                if(ped.getUsuarioByAutorizo()!=null && ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(ped.getUsuarioByAutorizo().getEmpleado().getNombre(), ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("       NO AUTORIZADO","       NO AUTORIZADO");
                cabeceraCompra(reporte, bf, tabla, ped);

                Partida[] cuentas =(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("pedido.idPedido", Integer.parseInt(no_ped))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);

                int ren=0;
                double total=0d;
                if(cuentas.length>0)
                {
                    for(int i=0; i<cuentas.length; i++)
                    {
                        int r=i+1;
                        //consecutivo
                        tabla.addCell(reporte.celda(""+r, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getCantPcp()>0)//cantidad a comprar
                            tabla.addCell(reporte.celda(""+cuentas[i].getCantPcp(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda("0", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        //Descripcion
                        String anotacion="";
                        if(cuentas[i].getInstruccion()!=null)
                            anotacion=cuentas[i].getInstruccion();
                        tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre()+" "+anotacion, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        //folio del articulo-partida-subpartida
                        tabla.addCell(reporte.celda(""+cuentas[i].getCatalogo().getIdCatalogo()+"-"+cuentas[i].getIdEvaluacion()+ "-"+cuentas[i].getSubPartida(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getEjemplar()!=null)
                        {
                            //No de parte
                            tabla.addCell(reporte.celda(""+cuentas[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        }
                        else
                        tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getPcp()!=null)//costo unit
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getPcp()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getCantPcp()>0 && cuentas[i].getPcp()!=null)//costo total
                        {
                            double sum=cuentas[i].getCantPcp() * cuentas[i].getPcp();
                            total+=sum;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        }
                        else
                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        ren++;
                    }
                }
                if(ped.getNotas()!=null)
                    tabla.addCell(reporte.celda("Notas:"+ped.getNotas(), font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                else
                    tabla.addCell(reporte.celda("Notas:", font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                switch(ped.getFormaPago())
                {
                    case 1:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Credito]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 2:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Contado]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                    
                    case 3:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Efectivo]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 4:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Cheque]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                }
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 3,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                double iva=total*0.16d;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("[NO SE RECIBIRÁ MATERIAL EN ALMACÉN SIN ESTA ORDEN DE COMPRA Y REMISIÓN O FACTURA CORRESPONDIENTE]", font, contenido, centro, 7,1,Rectangle.NO_BORDER));
                tabla.setHeaderRows(1);
                session.beginTransaction().rollback();

                reporte.agregaObjeto(tabla);
                
                reporte.cerrar();
                reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-orden.pdf");

            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
            }
    }
    
    void ordenCompraDCG(String tipo)
    {
        h=new Herramientas(usr, 0);
            h.session(sessionPrograma);
            session = HibernateUtil.getSessionFactory().openSession();
            ord=(Orden)session.get(Orden.class, ord.getIdOrden());
            try
            {
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
                formatoPorcentaje.setMinimumFractionDigits(2);

                session.beginTransaction().begin();
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

                PDF reporte = new PDF();
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                File folder = new File("reportes/"+ord.getIdOrden());
                folder.mkdirs();
                reporte.Abrir2(PageSize.LETTER, "cabecera", "reportes/"+ord.getIdOrden()+"/"+valor+"-ordenDCG.pdf");
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;
                float tam[]=new float[]{20,20,40,220,90,50,50};
                PdfPTable tabla=reporte.crearTabla(7, tam, 100, Element.ALIGN_LEFT);

                Pedido ped = (Pedido)session.get(Pedido.class, Integer.parseInt(no_ped));
                reporte.estatusAutoriza(ped.getEmpleado().getNombre(),"");
                cabeceraCompraDCG(reporte, bf, tabla, ped, tipo);

                Partida[] cuentas =(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("pedido.idPedido", Integer.parseInt(no_ped))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);

                int ren=0;
                double total=0d;
                if(cuentas.length>0)
                {
                    for(int i=0; i<cuentas.length; i++)
                    {
                        int r=i+1;
                        //consecutivo
                        tabla.addCell(reporte.celda(""+r, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getCantPcp()>0)//cantidad a comprar
                            tabla.addCell(reporte.celda(""+cuentas[i].getCantPcp(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda("0", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getMed()!=null)//Unidad
                            tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //Descripcion
                        String anotacion="";
                        if(cuentas[i].getInstruccion()!=null)
                            anotacion=cuentas[i].getInstruccion();
                        tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre()+" "+anotacion, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getEjemplar()!=null)
                        {
                            //No de parte
                            tabla.addCell(reporte.celda(""+cuentas[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        }
                        else
                            tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getPcp()!=null)//costo unit
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getPcp()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(cuentas[i].getCantPcp()>0 && cuentas[i].getPcp()!=null)//costo total
                        {
                            double sum=cuentas[i].getCantPcp() * cuentas[i].getPcp();
                            total+=sum;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        }
                        else
                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        ren++;
                    }
                }
                if(ped.getNotas()!=null)
                    tabla.addCell(reporte.celda("Notas:"+ped.getNotas(), font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                else
                    tabla.addCell(reporte.celda("Notas:", font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                switch(ped.getFormaPago())
                {
                    case 1:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Credito]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 2:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Contado]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                    
                    case 3:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Efectivo]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 4:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Cheque]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                }
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 2,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                double iva=total*0.16d;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.setHeaderRows(1);
                session.beginTransaction().rollback();

                reporte.agregaObjeto(tabla);
                
                reporte.cerrar();
                reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-ordenDCG.pdf");

            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
            }
    }
    
    void ordenCompraExternosDCG(int pedido, String tipo)
    {
        h=new Herramientas(usr, 0);
            h.session(sessionPrograma);
            session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
                formatoPorcentaje.setMinimumFractionDigits(2);

                session.beginTransaction().begin();
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

                PDF reporte = new PDF();
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                File folder = new File("reportes/externos");
                folder.mkdirs();
                reporte.Abrir2(PageSize.LETTER, "cabecera", "reportes/externos/"+valor+"-ordenDCG.pdf");
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;
                float tam[]=new float[]{20,20,40,220,90,50,50};
                PdfPTable tabla=reporte.crearTabla(7, tam, 100, Element.ALIGN_LEFT);

                Pedido ped = (Pedido)session.get(Pedido.class, pedido);
                
                reporte.estatusAutoriza(ped.getEmpleado().getNombre(),"");
                if(ped.getUsuarioByAutorizo()!=null && ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(ped.getUsuarioByAutorizo().getEmpleado().getNombre(), ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("       NO AUTORIZADO","       NO AUTORIZADO");
                
                if(ped.getTipoPedido().compareToIgnoreCase("Externo")==0 || ped.getTipoPedido().compareToIgnoreCase("Inventario")==0)
                    cabeceraCompraExDCG(reporte, bf, tabla, ped, tipo);
                if(ped.getTipoPedido().compareToIgnoreCase("Adicional")==0)
                    cabeceraCompraDCG(reporte, bf, tabla, ped, tipo);

                PartidaExterna[] cuentas =(PartidaExterna[]) session.createCriteria(PartidaExterna.class).
                        add(Restrictions.eq("pedido.idPedido", pedido)).
                        addOrder(Order.asc("idPartidaExterna")).
                        list().toArray(new PartidaExterna[0]);
                int ren=0;
                double total=0d;
                if(cuentas.length>0)
                {
                    for(int i=0; i<cuentas.length; i++)
                    {
                        int r=i+1;
                        //consecutivo
                        tabla.addCell(reporte.celda(""+r, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        //cantidad a comprar
                        tabla.addCell(reporte.celda(""+cuentas[i].getCantidad(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        //Unidad
                        tabla.addCell(reporte.celda(cuentas[i].getUnidad(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //Descripcion
                        tabla.addCell(reporte.celda(cuentas[i].getDescripcion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        if(ped.getTipoPedido().compareTo("Inventario")==0)
                            tabla.addCell(reporte.celda(""+cuentas[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                           tabla.addCell(reporte.celda(""+cuentas[i].getNoParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //costo unit
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCosto()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        double sum=cuentas[i].getCantidad() * cuentas[i].getCosto();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        ren++;
                    }
                }
                if(ped.getNotas()!=null)
                    tabla.addCell(reporte.celda("Notas:"+ped.getNotas(), font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                else
                    tabla.addCell(reporte.celda("Notas:", font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                switch(ped.getFormaPago())
                {
                    case 1:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Credito]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 2:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Contado]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                    
                    case 3:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Efectivo]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 4:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Cheque]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                        break;
                }
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 2,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                double iva=total*0.16d;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.setHeaderRows(1);
                session.beginTransaction().rollback();

                reporte.agregaObjeto(tabla);
                
                reporte.cerrar();
                reporte.visualizar2("reportes/externos/"+valor+"-ordenDCG.pdf");

            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
            }
    }
    
    void ordenCompraExternos(int pedido)
    {
        h=new Herramientas(usr, 0);
            h.session(sessionPrograma);
            session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
                formatoPorcentaje.setMinimumFractionDigits(2);

                session.beginTransaction().begin();
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

                PDF reporte = new PDF();
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                File folder = new File("reportes/externos");
                folder.mkdirs();
                reporte.Abrir2(PageSize.LETTER, "cabecera", "reportes/externos/"+valor+"-orden.pdf");
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;
                float tam[]=new float[]{20,20,220,40,90,50,50};
                PdfPTable tabla=reporte.crearTabla(7, tam, 100, Element.ALIGN_LEFT);

                Pedido ped = (Pedido)session.get(Pedido.class, pedido);
                
                if(ped.getUsuarioByAutorizo()!=null && ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(ped.getUsuarioByAutorizo().getEmpleado().getNombre(), ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("       NO AUTORIZADO","       NO AUTORIZADO");
                if(ped.getTipoPedido().compareToIgnoreCase("Externo")==0 || ped.getTipoPedido().compareToIgnoreCase("Inventario")==0)
                    cabeceraCompraEx(reporte, bf, tabla, ped);
                if(ped.getTipoPedido().compareToIgnoreCase("Adicional")==0)
                    cabeceraCompra(reporte, bf, tabla, ped);

                PartidaExterna[] cuentas =(PartidaExterna[]) session.createCriteria(PartidaExterna.class).
                        add(Restrictions.eq("pedido.idPedido", pedido)).
                        addOrder(Order.asc("idPartidaExterna")).
                        list().toArray(new PartidaExterna[0]);
                int ren=0;
                double total=0d;
                if(cuentas.length>0)
                {
                    for(int i=0; i<cuentas.length; i++)
                    {
                        int r=i+1;
                        //consecutivo
                        tabla.addCell(reporte.celda(""+r, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        //cantidad a comprar
                        tabla.addCell(reporte.celda(""+cuentas[i].getCantidad(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //Descripcion
                        tabla.addCell(reporte.celda(cuentas[i].getDescripcion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        //folio del articulo-partida-subpartida
                        if(cuentas[i].getPartida()!=null)
                            tabla.addCell(reporte.celda(cuentas[i].getPartida()+ "-"+cuentas[i].getIdValuacion(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(ped.getTipoPedido().compareTo("Inventario")==0)
                            tabla.addCell(reporte.celda(""+cuentas[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda(""+cuentas[i].getNoParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //costo unit
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCosto()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        double sum=cuentas[i].getCantidad() * cuentas[i].getCosto();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        ren++;
                    }
                }
                if(ped.getNotas()!=null)
                    tabla.addCell(reporte.celda("Notas:"+ped.getNotas(), font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                else
                    tabla.addCell(reporte.celda("Notas:", font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                switch(ped.getFormaPago())
                {
                    case 1:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Credito]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 2:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Contado]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                    
                    case 3:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Efectivo]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                        
                    case 4:
                        tabla.addCell(reporte.celda("[Los montos estan en Pesos, Forma de pago Cheque]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                        break;
                }
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 3,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                double iva=total*0.16d;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 6,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("[NO SE RECIBIRÁ MATERIAL EN ALMACÉN SIN ESTA ORDEN DE COMPRA Y REMISIÓN O FACTURA CORRESPONDIENTE]", font, contenido, centro, 7,1,Rectangle.NO_BORDER));
                tabla.setHeaderRows(1);
                session.beginTransaction().rollback();

                reporte.agregaObjeto(tabla);
                
                reporte.cerrar();
                reporte.visualizar2("reportes/externos/"+valor+"-orden.pdf");

            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
            }
    }
    
    private void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla, Pedido ped)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 670, 240, 70, 5);
        reporte.contenido.roundRectangle(280, 670, 293, 70, 5);
        reporte.contenido.roundRectangle(35, 655, 540, 11, 3);

        Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 14);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 760, 0);
        reporte.contenido.setFontAndSize(bf, 12);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pedido a Proveedores", 35, 745, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Datos de Facturación", 450, 730, 0);
        reporte.contenido.setFontAndSize(bf, 9);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 745, 0);
                
        //ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 
       
        //************************datos del proveedor****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pedido: "+ped.getIdPedido(), 40, 725, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha: "+dateFormat.format(ped.getFechaPedido()), 190, 725, 0);
        String nomb=ped.getProveedorByIdProveedor().getNombre();
        if(nomb.length()>40)
            nomb=nomb.substring(0,39);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Prov: "+nomb, 40, 715, 0);
        

        if(ord!=null)
        {
            /*if(ord.getCompania()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Aseg:"+ord.getCompania().getNombre(), 40, 705, 0);*/
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "O. Taller: "+ord.getIdOrden()+"    Modelo: "+ord.getModelo(), 40, 695, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ord.getTipo().getTipoNombre(), 40, 685, 0);
            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Serie: "+ord.getNoSerie(), 40, 675, 0);
            
        }
        else
        {
            if(ped.getTipoPedido().compareTo("Inventario")==0){
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Aseg: NA", 40, 705, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inventario", 40, 695, 0);
            }else{
                /*if(ped.getOrdenExterna().getCompania()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Aseg: NA"+ped.getOrdenExterna().getCompania().getNombre(), 40, 705, 0);*/
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Externo", 40, 695, 0);
            }
        }
        
        //**********************datos de facturacion*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Clave: "+ped.getProveedorByIdEmpresa().getIdProveedor(), 285, 725, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomb: "+ped.getProveedorByIdEmpresa().getNombre(), 284, 715, 0);
        if(ped.getProveedorByIdEmpresa().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir: "+ped.getProveedorByIdEmpresa().getDireccion(), 295, 705, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 295, 705, 0);
        if(ped.getProveedorByIdEmpresa().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdEmpresa().getColonia(), 293, 695, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 293, 695, 0);
        if(ped.getProveedorByIdEmpresa().getPoblacion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob: "+ped.getProveedorByIdEmpresa().getPoblacion(), 291, 685, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob:", 291, 685, 0);
        if(ped.getProveedorByIdEmpresa().getCp()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP: "+ped.getProveedorByIdEmpresa().getCp(), 500, 685, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP:", 500, 685, 0);
        if(ped.getProveedorByIdEmpresa().getRfc()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+ped.getProveedorByIdEmpresa().getRfc(), 289, 675, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 289, 675, 0);
        
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.finTexto();
        
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            tabla.addCell(reporte.celda("#", font, null, centro, 0, 1, Rectangle.NO_BORDER));
            tabla.addCell(reporte.celda("Part P.", font, null, centro, 0, 1, Rectangle.LEFT));
            tabla.addCell(reporte.celda("Folio", font, null, centro, 0, 1,Rectangle.LEFT));
            tabla.addCell(reporte.celda("No° Parte", font, null, centro, 0, 1, Rectangle.LEFT));
            tabla.addCell(reporte.celda("Descripción", font, null, centro, 0, 1,Rectangle.LEFT));
            tabla.addCell(reporte.celda("Med", font, null, centro, 0, 1,Rectangle.LEFT));
            tabla.addCell(reporte.celda("T.E.", font, null, centro, 0, 1, Rectangle.LEFT));
            tabla.addCell(reporte.celda("Cant.", font, null, centro, 0, 1, Rectangle.LEFT));
            tabla.addCell(reporte.celda("Costo C/U", font, null, centro, 0, 1, Rectangle.LEFT));
            tabla.addCell(reporte.celda("Total", font, null, centro, 0, 1, Rectangle.LEFT));            
    }
    
    private void cabeceraCompra(PDF reporte, BaseFont bf, PdfPTable tabla, Pedido ped)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(35, 625, 180, 80, 0);
        
        reporte.contenido.roundRectangle(215, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(215, 625, 180, 80, 0);

        reporte.contenido.roundRectangle(395, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(395, 625, 180, 80, 0);
        
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 13);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.agregaObjeto(reporte.crearImagen("imagenes/grande300115.jpg", 30, -40, 60));
        reporte.contenido.setFontAndSize(bf, 12);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ORDEN DE COMPRA: "+ped.getIdPedido(), 35, 710, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 710, 0);
                
        ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 
       
        //************************datos del proveedor****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL PROVEEDOR", 73, 697, 0);
        String nomb=ped.getProveedorByIdProveedor().getNombre();
        if(ped.getProveedorByIdProveedor().getNombre().length()>37)
            nomb=nomb.substring(0, 36);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, nomb, 40, 687, 0);
        
        if(ped.getProveedorByIdProveedor().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdProveedor().getDireccion(), 40, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 40, 677, 0);
        
        if(ped.getProveedorByIdProveedor().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdProveedor().getColonia(), 40, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 40, 667, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Edo: "+ped.getProveedorByIdProveedor().getEstado(), 40, 657, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel: "+ped.getProveedorByIdProveedor().getTel1(), 40, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel:", 40, 647, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto: "+ped.getProveedorByIdProveedor().getRepresentante(), 40, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto:", 40, 637, 0);
        
        if(ped.getProveedorByIdProveedor().getEmail()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: "+ped.getProveedorByIdProveedor().getEmail(), 40, 627, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: ", 40, 627, 0);

        
        //**********************datos de facturacion*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE FACTURACIÓN", 250, 697, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getNombre(), 220, 687, 0);
        if(ped.getProveedorByIdEmpresa().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getDireccion(), 220, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 220, 677, 0);
        if(ped.getProveedorByIdEmpresa().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdEmpresa().getColonia(), 220, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 220, 667, 0);
        if(ped.getProveedorByIdEmpresa().getPoblacion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob: "+ped.getProveedorByIdEmpresa().getPoblacion(), 220, 657, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob:", 220, 657, 0);
        if(ped.getProveedorByIdEmpresa().getCp()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP: "+ped.getProveedorByIdEmpresa().getCp(), 220, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP:", 220, 647, 0);
        if(ped.getProveedorByIdEmpresa().getRfc()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+ped.getProveedorByIdEmpresa().getRfc(), 220, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 220, 537, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS LA UNIDAD", 450, 697, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ord.getIdOrden(), 410, 687, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ord.getTipo().getTipoNombre(), 410, 677, 0);
        if(ord.getNoSerie()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Serie: "+ord.getNoSerie(), 410, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Serie: ", 410, 667, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ord.getModelo(), 410, 657, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: "+ord.getCompania().getNombre(), 410, 647, 0);
        String clien=ord.getClientes().getNombre();
        if(clien.length()>25)
            clien=clien.substring(0, 25);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:"+clien, 410, 637, 0);
        if(ord.getSiniestro()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ord.getSiniestro(), 410, 627, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 627, 0);
        reporte.contenido.setFontAndSize(bf, 12);
        reporte.finTexto();
        reporte.contenido.setFontAndSize(bf, 12);
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
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
        
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Part P.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("No° Parte", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Costo C/U", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));

    }
    
    private void cabeceraCompraDCG(PDF reporte, BaseFont bf, PdfPTable tabla, Pedido ped, String tipo)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(35, 625, 180, 80, 0);
        
        reporte.contenido.roundRectangle(215, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(215, 625, 180, 80, 0);

        reporte.contenido.roundRectangle(395, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(395, 625, 180, 80, 0);
        
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 13);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.agregaObjeto(reporte.crearImagen("imagenes/volvo.png", 0, -35, 30));
        reporte.agregaObjeto(reporte.crearImagen("imagenes/mack.png", 410, -30, 30));
        reporte.contenido.setFontAndSize(bf, 12);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Distribuidora de Camiones Guerrero, S.A. de C.V.", 110, 770, 0);
        if(ped.getIdExterno()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, tipo+": "+ped.getIdExterno(), 35, 710, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, tipo+": NA", 35, 710, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Av. Tlalnepantla No. 58 ", 110, 760, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Barrio La Concepción", 110, 750, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tultitlán, Estado de México", 110, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "C.P. 54900", 110, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel. 199 24 04 / 590 12 29", 110, 720, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 710, 0);
                
        ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 
       
        //************************datos del proveedor****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL PROVEEDOR", 73, 697, 0);
        String nomb=ped.getProveedorByIdProveedor().getNombre();
        if(ped.getProveedorByIdProveedor().getNombre().length()>37)
            nomb=nomb.substring(0, 36);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, nomb, 40, 687, 0);
        
        if(ped.getProveedorByIdProveedor().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdProveedor().getDireccion(), 40, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 40, 677, 0);
        
        if(ped.getProveedorByIdProveedor().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdProveedor().getColonia(), 40, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 40, 667, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Edo: "+ped.getProveedorByIdProveedor().getEstado(), 40, 657, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel: "+ped.getProveedorByIdProveedor().getTel1(), 40, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel:", 40, 647, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto: "+ped.getProveedorByIdProveedor().getRepresentante(), 40, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto:", 40, 637, 0);
        
        if(ped.getProveedorByIdProveedor().getEmail()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: "+ped.getProveedorByIdProveedor().getEmail(), 40, 627, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: ", 40, 627, 0);

        
        //**********************datos de facturacion*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE FACTURACIÓN", 250, 697, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getNombre(), 220, 687, 0);
        if(ped.getProveedorByIdEmpresa().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getDireccion(), 220, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 220, 677, 0);
        if(ped.getProveedorByIdEmpresa().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdEmpresa().getColonia(), 220, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 220, 667, 0);
        if(ped.getProveedorByIdEmpresa().getPoblacion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob: "+ped.getProveedorByIdEmpresa().getPoblacion(), 220, 657, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob:", 220, 657, 0);
        if(ped.getProveedorByIdEmpresa().getCp()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP: "+ped.getProveedorByIdEmpresa().getCp(), 220, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP:", 220, 647, 0);
        if(ped.getProveedorByIdEmpresa().getRfc()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+ped.getProveedorByIdEmpresa().getRfc(), 220, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 220, 537, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS LA UNIDAD", 450, 697, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ord.getIdOrden(), 410, 687, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ord.getTipo().getTipoNombre(), 410, 677, 0);
        if(ord.getNoSerie()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Serie: "+ord.getNoSerie(), 410, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Serie: ", 410, 667, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ord.getModelo(), 410, 657, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: "+ord.getCompania().getNombre(), 410, 647, 0);
        String clien=ord.getClientes().getNombre();
        if(clien.length()>25)
            clien=clien.substring(0, 25);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:"+clien, 410, 637, 0);
        if(ord.getSiniestro()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ord.getSiniestro(), 410, 627, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 627, 0);
        
        reporte.contenido.setFontAndSize(bf, 12);
        
        reporte.finTexto();
        reporte.contenido.setFontAndSize(bf, 12);
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
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
        
            tabla.addCell(reporte.celda("Part.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Uni.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("No° Parte", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Costo C/U", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));

    }
    
    private void cabeceraCompraExDCG(PDF reporte, BaseFont bf, PdfPTable tabla, Pedido ped, String tipo)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(35, 625, 180, 80, 0);
        
        reporte.contenido.roundRectangle(215, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(215, 625, 180, 80, 0);

        reporte.contenido.roundRectangle(395, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(395, 625, 180, 80, 0);
        
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 13);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.agregaObjeto(reporte.crearImagen("imagenes/volvo.png", 0, -35, 30));
        reporte.agregaObjeto(reporte.crearImagen("imagenes/mack.png", 410, -30, 30));
        reporte.contenido.setFontAndSize(bf, 12);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Distribuidora de Camiones Guerrero, S.A. de C.V.", 110, 770, 0);
        if(ped.getIdExterno()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, tipo+": "+ped.getIdExterno(), 35, 710, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, tipo+": NA", 35, 710, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Av. Tlalnepantla No. 58", 110, 760, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Barrio La Concepción", 110, 750, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tultitlán, Estado de México", 110, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "C.P. 54900", 110, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel. 199 24 04 / 590 12 29", 110, 720, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 710, 0);
                
        //ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 
       
        //************************datos del proveedor****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL PROVEEDOR", 73, 697, 0);
        
        String nomb=ped.getProveedorByIdProveedor().getNombre();
        if(nomb.length()>37)
            nomb=nomb.substring(0, 36);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, nomb, 40, 687, 0);
        
        if(ped.getProveedorByIdProveedor().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdProveedor().getDireccion(), 40, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 40, 677, 0);
        
        if(ped.getProveedorByIdProveedor().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdProveedor().getColonia(), 40, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 40, 667, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Edo: "+ped.getProveedorByIdProveedor().getEstado(), 40, 657, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel: "+ped.getProveedorByIdProveedor().getTel1(), 40, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel:", 40, 647, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto: "+ped.getProveedorByIdProveedor().getRepresentante(), 40, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto:", 40, 637, 0);
        
        if(ped.getProveedorByIdProveedor().getEmail()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: "+ped.getProveedorByIdProveedor().getEmail(), 40, 627, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: ", 40, 627, 0);

        
        //**********************datos de facturacion*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE FACTURACIÓN", 250, 697, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getNombre(), 220, 687, 0);
        if(ped.getProveedorByIdEmpresa().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getDireccion(), 220, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 220, 677, 0);
        if(ped.getProveedorByIdEmpresa().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdEmpresa().getColonia(), 220, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 220, 667, 0);
        if(ped.getProveedorByIdEmpresa().getPoblacion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob: "+ped.getProveedorByIdEmpresa().getPoblacion(), 220, 657, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob:", 220, 657, 0);
        if(ped.getProveedorByIdEmpresa().getCp()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP: "+ped.getProveedorByIdEmpresa().getCp(), 220, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP:", 220, 647, 0);
        if(ped.getProveedorByIdEmpresa().getRfc()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+ped.getProveedorByIdEmpresa().getRfc(), 220, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 220, 537, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS LA UNIDAD", 450, 697, 0);
        if(ped.getPartida()==null)
        {
            if(ped.getTipoPedido().compareTo("Inventario")==0)
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: NA", 410, 687, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: NA", 410, 677, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: NA", 410, 667, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: NA", 410, 657, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado: NA", 410, 647, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro: NA", 410, 637, 0);
            }
            else
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ped.getOrdenExterna().getIdOrden(), 410, 687, 0);
                if(ped.getOrdenExterna().getTipo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ped.getOrdenExterna().getTipo().getTipoNombre(), 410, 677, 0);

                if(ped.getOrdenExterna().getModelo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ped.getOrdenExterna().getModelo(), 410, 667, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: ", 410, 667, 0);

                if(ped.getOrdenExterna().getCompania()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: "+ped.getOrdenExterna().getCompania().getNombre(), 410, 657, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: ", 410, 657, 0);

                if(ped.getOrdenExterna().getAsegurado()!=null)
                {
                    String clien=ped.getOrdenExterna().getAsegurado();
                    if(clien.length()>25)
                        clien=clien.substring(0, 25);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:"+clien, 410, 647, 0);
                }
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:", 410, 647, 0);

                if(ped.getOrdenExterna().getSiniestro()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ped.getOrdenExterna().getSiniestro(), 410, 637, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 637, 0);
            }
        }
        else
        {
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ped.getPartida().getOrdenByIdOrden().getIdOrden(), 410, 687, 0);
            if(ped.getPartida().getOrdenByIdOrden().getTipo()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ped.getPartida().getOrdenByIdOrden().getTipo().getTipoNombre(), 410, 677, 0);

            if(ped.getPartida().getOrdenByIdOrden().getModelo()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ped.getPartida().getOrdenByIdOrden().getModelo(), 410, 667, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: ", 410, 667, 0);

            if(ped.getPartida().getOrdenByIdOrden().getCompania()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: "+ped.getPartida().getOrdenByIdOrden().getCompania().getNombre(), 410, 657, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: ", 410, 657, 0);
            
            if(ped.getPartida().getOrdenByIdOrden().getClientes()!=null)
            {
                String clien=ped.getPartida().getOrdenByIdOrden().getClientes().getNombre();
                if(clien.length()>25)
                    clien=clien.substring(0, 25);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:"+clien, 410, 647, 0);
            }
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:", 410, 647, 0);

            if(ped.getPartida().getOrdenByIdOrden().getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ped.getPartida().getOrdenByIdOrden().getSiniestro(), 410, 637, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 637, 0);
        }
        
        reporte.contenido.setColorFill(BaseColor.BLACK);
        
        reporte.finTexto();
        reporte.contenido.setFontAndSize(bf, 12);
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
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
        
            tabla.addCell(reporte.celda("Part.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Uni.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("No° Parte", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Costo C/U", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
    }
    
    private void cabeceraCompraEx(PDF reporte, BaseFont bf, PdfPTable tabla, Pedido ped)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(35, 625, 180, 80, 0);
        
        reporte.contenido.roundRectangle(215, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(215, 625, 180, 80, 0);

        reporte.contenido.roundRectangle(395, 695, 180, 10, 0);
        reporte.contenido.roundRectangle(395, 625, 180, 80, 0);
        
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 13);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.agregaObjeto(reporte.crearImagen("imagenes/grande300115.jpg", 30, -40, 60));
        reporte.contenido.setFontAndSize(bf, 12);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ORDEN DE COMPRA: "+ped.getIdPedido(), 35, 710, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 710, 0);
       
        //************************datos del proveedor****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL PROVEEDOR", 73, 697, 0);
        
        String nomb=ped.getProveedorByIdProveedor().getNombre();
        if(nomb.length()>37)
            nomb=nomb.substring(0, 36);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, nomb, 40, 687, 0);
        
        if(ped.getProveedorByIdProveedor().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdProveedor().getDireccion(), 40, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 40, 677, 0);
        
        if(ped.getProveedorByIdProveedor().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdProveedor().getColonia(), 40, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 40, 667, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Edo: "+ped.getProveedorByIdProveedor().getEstado(), 40, 657, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel: "+ped.getProveedorByIdProveedor().getTel1(), 40, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel:", 40, 647, 0);
        
        if(ped.getProveedorByIdProveedor().getTel1()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto: "+ped.getProveedorByIdProveedor().getRepresentante(), 40, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto:", 40, 637, 0);
        
        if(ped.getProveedorByIdProveedor().getEmail()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: "+ped.getProveedorByIdProveedor().getEmail(), 40, 627, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: ", 40, 627, 0);

        
        //**********************datos de facturacion*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE FACTURACIÓN", 250, 697, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getNombre(), 220, 687, 0);
        if(ped.getProveedorByIdEmpresa().getDireccion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ped.getProveedorByIdEmpresa().getDireccion(), 220, 677, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 220, 677, 0);
        if(ped.getProveedorByIdEmpresa().getColonia()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+ped.getProveedorByIdEmpresa().getColonia(), 220, 667, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 220, 667, 0);
        if(ped.getProveedorByIdEmpresa().getPoblacion()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob: "+ped.getProveedorByIdEmpresa().getPoblacion(), 220, 657, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob:", 220, 657, 0);
        if(ped.getProveedorByIdEmpresa().getCp()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP: "+ped.getProveedorByIdEmpresa().getCp(), 220, 647, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP:", 220, 647, 0);
        if(ped.getProveedorByIdEmpresa().getRfc()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+ped.getProveedorByIdEmpresa().getRfc(), 220, 637, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 220, 537, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS LA UNIDAD", 450, 697, 0);
        if(ped.getPartida()==null)
        {
            if(ped.getTipoPedido().compareTo("Inventario")==0)
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: NA", 410, 687, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: NA", 410, 677, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: NA", 410, 667, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: NA", 410, 657, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado: NA", 410, 647, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro: NA", 410, 637, 0);
            }
            else
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ped.getOrdenExterna().getIdOrden(), 410, 687, 0);
                if(ped.getOrdenExterna().getTipo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ped.getOrdenExterna().getTipo().getTipoNombre(), 410, 677, 0);

                if(ped.getOrdenExterna().getModelo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ped.getOrdenExterna().getModelo(), 410, 667, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: ", 410, 667, 0);

                if(ped.getOrdenExterna().getCompania()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: "+ped.getOrdenExterna().getCompania().getNombre(), 410, 657, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: ", 410, 657, 0);

                if(ped.getOrdenExterna().getAsegurado()!=null)
                {
                    String clien=ped.getOrdenExterna().getAsegurado();
                    if(clien.length()>25)
                        clien=clien.substring(0, 25);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:"+clien, 410, 647, 0);
                }
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:", 410, 647, 0);

                if(ped.getOrdenExterna().getSiniestro()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ped.getOrdenExterna().getSiniestro(), 410, 637, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 637, 0);
            }
        }
        else
        {
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ped.getPartida().getOrdenByIdOrden().getIdOrden(), 410, 687, 0);
            if(ped.getPartida().getOrdenByIdOrden().getTipo()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ped.getPartida().getOrdenByIdOrden().getTipo().getTipoNombre(), 410, 677, 0);

            if(ped.getPartida().getOrdenByIdOrden().getModelo()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ped.getPartida().getOrdenByIdOrden().getModelo(), 410, 667, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: ", 410, 667, 0);

            if(ped.getPartida().getOrdenByIdOrden().getCompania()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: "+ped.getPartida().getOrdenByIdOrden().getCompania().getNombre(), 410, 657, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañía: ", 410, 657, 0);
            
            if(ped.getPartida().getOrdenByIdOrden().getClientes()!=null)
            {
                String clien=ped.getPartida().getOrdenByIdOrden().getClientes().getNombre();
                if(clien.length()>25)
                    clien=clien.substring(0, 25);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:"+clien, 410, 647, 0);
            }
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asegurado:", 410, 647, 0);

            if(ped.getPartida().getOrdenByIdOrden().getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ped.getPartida().getOrdenByIdOrden().getSiniestro(), 410, 637, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 637, 0);
        }
        
        reporte.contenido.setColorFill(BaseColor.BLACK);
        
        reporte.finTexto();
        reporte.contenido.setFontAndSize(bf, 12);
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
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
        
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Part P.", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("No° Parte", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Costo C/U", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
    }
    
    private void cabeceraPre(PDF reporte, BaseFont bf, PdfPTable tabla)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 660, 543, 40, 5);//cuadro cliente
        reporte.contenido.roundRectangle(35, 618, 543, 40, 5);//cuadro unidad
        
        reporte.contenido.roundRectangle(353, 738, 223, 10, 0);//cuadro fecha
        reporte.contenido.roundRectangle(353, 728, 223, 10, 0);//cuadro F.Fiscal
        reporte.contenido.roundRectangle(353, 718, 223, 10, 0);//cuadro C. SAT
        reporte.contenido.roundRectangle(353, 708, 223, 10, 0);//cuadro C. Emisor

        Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
        
        reporte.inicioTexto();
        reporte.agregaObjeto(reporte.crearImagen("imagenes/"+con.getFacturaLogo(), 00, -32, 40, true));
        reporte.contenido.setFontAndSize(bf, 10);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "FACTURA", 520, 765, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "", 520, 755, 0);
        reporte.contenido.setFontAndSize(bf, 8);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:", 425, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 430, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Folio Fiscal:", 425, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Certificado SAT:", 425, 720, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Certificado Emisor:", 425, 710, 0);
        reporte.contenido.setFontAndSize(bf, 6);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getDireccion()+" "+con.getNo()+" "+con.getColonia()+" "+con.getMunicipio()+" "+con.getEstado()+" CP "+con.getCp(), 40, 702, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Tel. (01 722) 199-24- 04", 570, 702, 0);
        
        ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 
        reporte.contenido.setFontAndSize(bf, 8);
        //************************datos del cliente****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Cliente: ", 80, 692, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Direccion: ", 80, 682, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Ciudad: ", 80, 672, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "R.F.C.: ", 80, 662, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Marca: ", 80, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Tipo: ", 80, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "No. Serie: ", 80, 630, 0);
        
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo: ", 350, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Placas: ", 350, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Poliza: ", 350, 630, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Km: ", 350, 620, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Sinisestro: ", 490, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "INC: ", 490, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Reporte: ", 490, 630, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "N°Eco: ", 490, 620, 0);

        if(factura==null)
        {
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getMarca().getMarcaNombre(), 80, 650, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getTipo().getTipoNombre(), 80, 640, 0);
            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoSerie(), 80, 630, 0);
            
            if(ord.getNoEconomico()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoEconomico(), 490, 620, 0);
            
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getModelo(), 350, 650, 0);
            if(ord.getNoPlacas()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoPlacas(), 350, 640, 0);
            if(ord.getPoliza()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getPoliza(), 350, 630, 0);
            
            if(ord.getTipoCliente().compareTo("2")==0 || ord.getTipoCliente().compareTo("3")==0)
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Tercero: ", 80, 620, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getClientes().getNombre(), 80, 620, 0);
            }
            else
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Asegurado: ", 80, 620, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getClientes().getNombre(), 80, 620, 0);
            }
            
            if(ord.getKm()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getKm(), 365, 620, 0);
            
            if(ord.getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getSiniestro(), 490, 650, 0);
            if(ord.getInciso()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getInciso(), 490, 640, 0);
            if(ord.getNoReporte()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoReporte(), 490, 630, 0);
        }
        reporte.finTexto();
        
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            Font font_mini = new Font(Font.FontFamily.HELVETICA, 1, Font.BOLD);
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            tabla.addCell(reporte.celda("Cant", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP));
            tabla.addCell(reporte.celda("U/Med", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP));
            tabla.addCell(reporte.celda("D E S C R I P C I O N", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP));
            tabla.addCell(reporte.celda("Precio c/u", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP));
            tabla.addCell(reporte.celda("T O T A L", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP));
            tabla.addCell(reporte.celda(" ", font_mini, null, centro, 5, 1,Rectangle.BOTTOM));
    }
    
    
    private void cabeceraFac(PDF reporte, BaseFont bf, PdfPTable tabla, Factura fac) throws DocumentException, IOException
    {
        BaseFont negrita = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.setColorStroke(new GrayColor(0.9f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.roundRectangle(35, 680, 543, 12, 5);//encabezado cliente
        if(factura.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
            reporte.contenido.roundRectangle(35, 625, 543, 12, 5);//encabezado unidad
        reporte.contenido.roundRectangle(353, 765, 223, 13, 5);//cuadro fecha
        reporte.contenido.fillStroke();
        reporte.contenido.roundRectangle(35, 642, 543, 50, 5);//cuadro cliente
        if(factura.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
            reporte.contenido.roundRectangle(35, 587, 543, 50, 5);//cuadro unidad
        reporte.contenido.roundRectangle(353, 701, 223, 77, 5);//cuadro C. Emisor
        /*reporte.contenido.roundRectangle(353, 728, 223, 10, 0);//cuadro F.Fiscal
        reporte.contenido.roundRectangle(353, 718, 223, 10, 0);//cuadro C. SAT*/
        

        //Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        
        reporte.inicioTexto();
        System.out.println(factura.getRfcEmisor());
        switch(factura.getRfcEmisor())
        {
            case "SET0806255W2":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa3666.jpg", 00, -65, 40));
                break;
            case "TBS160622L39":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/tbs.jpg", 00, -65, 40));
                break;
                
            case "PEVV740319ML1":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/torres.jpg", 00, -65, 40));
                break;    
            case "ITP150326MP1":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/imperia.jpg", 00, -65, 40));
                break;
            case "XTR190129S67":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa3666.jpg", 00, -65, 40));
                break;
        }
        
        //****************Datos de Emisor
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getNombreEmisor(), 120, 765, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+fac.getRfcEmisor(), 120, 757, 0);
        
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DOMICILIO FISCAL ", 120, 747, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getCalleEmisor()+" "+fac.getNumeroExteriorEmisor(), 120, 739, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getColoniaEmisor()+", "+fac.getMunicipioEmisor(), 120, 731, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getEstadoEmisor()+", "+fac.getPaisEmisor()+" "+fac.getCpEmisor(), 120, 724, 0);
        
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "LUGAR Y FECHA DE EXPEDICIÓN", 120, 714, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getMunicipioEmisor()+", "+fac.getEstadoEmisor(), 120, 706, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, dateFormat.format(fac.getFecha()), 120, 698, 0);
        
        reporte.contenido.setFontAndSize(negrita, 8);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FACTURA", 370, 769, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        if(fac.getFolio()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getSerie()+"-"+fac.getFolio(), 540, 769, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getSerieExterno()+"-"+fac.getFolioExterno(), 540, 769, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FOLIO FISCAL: "+fac.getFFiscal(), 360, 755, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FECHA CERTIFICACIÓN: "+fac.getFechaFiscal().substring(0, 19), 360, 746, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "MÉTODO DE PAGO: "+fac.getMetodoPago(), 360, 738, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° CTA. DE PAGO: "+fac.getCuentaPago(), 360, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO DE COMPROBANTE: Ingreso (I)", 360, 722, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NÚMERO INTERNO: "+fac.getIdFactura(), 360, 714, 0);
        if(fac.getVersion()>3.2)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "USO CFDI: "+fac.getUsoCfdi().getIdUsoCfdi(), 360, 706, 0);
        
        reporte.contenido.setFontAndSize(bf, 7);
        //************************datos del cliente****************************
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL CLIENTE", 260, 683, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOMBRE: "+fac.getNombreReceptor(), 40, 669, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+fac.getRfcReceptor(), 400, 669, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DOMICILIO FISCAL:    CALLE "+fac.getCalleReceptor()+" "+fac.getNumeroExteriorReceptor()+" COL. "+fac.getColoniaReceptor(), 40, 659, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "MPIO./DEL. "+fac.getMunicipioReceptor()+" ESTADO "+fac.getEstadoReceptor()+" pais "+fac.getPaisReceptor()+" CP "+fac.getCpReceptor(), 120, 649, 0);
        
        if(factura.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
        {
            //**********************datos de la unidad*****************************
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE LA UNIDAD", 260, 627, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "MARCA: ", 80, 617, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "TIPO: ", 80, 608, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "NO.SERIE: ", 80, 599, 0);
            if(fac.getOrden()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "ORDEN: ", 80, 590, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, " ", 80, 590, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "MODELO: ", 350, 617, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "PLACAS: ", 350, 608, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "POLIZA: ", 350, 599, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "KM: ", 350, 590, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "SINIESTRO: ", 490, 617, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "INC: ", 490, 608, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "REPORTE: ", 490, 599, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "COLOR: ", 490, 590, 0);
            

            if(fac.getOrden()!=null)
            {
                Orden ord=fac.getOrden();
                if(ord.getMarca()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getMarca().getMarcaNombre(), 80, 617, 0);
                if(ord.getTipo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getTipo().getTipoNombre(), 80, 608, 0);
                if(ord.getNoSerie()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoSerie(), 80, 599, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getIdOrden(), 80, 590, 0);
                
                if(ord.getModelo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getModelo(), 350, 617, 0);
                if(ord.getNoPlacas()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoPlacas(), 350, 608, 0);
                if(ord.getPoliza()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getPoliza(), 350, 599, 0);
                if(ord.getKm()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getKm(), 350, 590, 0);

                if(ord.getSiniestro()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getSiniestro(), 490, 617, 0);
                if(ord.getInciso()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getInciso(), 490, 608, 0);
                if(ord.getNoReporte()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoReporte(), 490, 599, 0);
                if(ord.getColor()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getColor(), 490, 590, 0);
                if(fac.getExtra()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getExtra(), 80, 580, 0);
                    
            }
            else
            {
                OrdenExterna ord=fac.getOrdenExterna();
                if(ord!=null)
                {
                    if(ord.getMarca()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getMarca().getMarcaNombre(), 80, 617, 0);
                    if(ord.getTipo()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getTipo().getTipoNombre(), 80, 608, 0);

                    if(ord.getNoSerie()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoSerie(), 80, 599, 0);
                    if(fac.getExtra()!=null)
                    {
                        if(ord.getNoEconomico()!=null)
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getExtra()+"   ECO: "+ord.getNoEconomico(), 80, 590, 0);
                        else
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getExtra(), 80, 590, 0);
                    }
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ECO: "+ord.getNoEconomico(), 80, 590, 0);

                    if(ord.getModelo()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getModelo(), 350, 617, 0);
                    if(ord.getNoPlacas()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoPlacas(), 350, 608, 0);
                    if(ord.getPoliza()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getPoliza(), 350, 599, 0);
                    if(ord.getKm()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getKm(), 350, 590, 0);

                    if(ord.getSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getSiniestro(), 490, 617, 0);
                    if(ord.getInciso()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getInciso(), 490, 608, 0);
                    if(ord.getNoReporte()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoReporte(), 490, 599, 0);
                    if(ord.getColor()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getColor(), 490, 590, 0);   
                }
            }
        }
        
        if(fac.getAddenda().compareTo("qualitas")==0)
        {
            if(factura.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
            {
                reporte.contenido.roundRectangle(35, 570, 543, 12, 5);//encabezado Agente Proveedor
                reporte.contenido.roundRectangle(35, 532, 543, 50, 5);//cuadro Agente Proveedor
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NO.PROV:"+fac.getNoProveedor(), 40, 561, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CONT. EMISOR:"+fac.getContactoEmisor(), 150, 561, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO:"+fac.getTipoEmisor(), 445, 561, 0);

                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "AREA:"+fac.getArea(), 40, 553, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "E-MAIL:"+fac.getCorreoEmisor(), 150, 553, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TEL:"+fac.getTelefonoEmisor(), 445, 553, 0);

                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CONT. RECEP:"+fac.getContactoReceptor(), 40, 544, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO:"+fac.getTipoReceptor(), 270, 544, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "E-MAIL:"+fac.getCorreoReceptor(), 370, 544, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOTAS:"+fac.getFoliosElectronicos(), 40, 535, 0);
            }
            else
            {
                reporte.contenido.roundRectangle(35, 620, 543, 12, 5);//encabezado Agente Proveedor
                reporte.contenido.roundRectangle(35, 582, 543, 50, 5);//cuadro Agente Proveedor
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NO.PROV:22194", 40, 610, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CONT. EMISOR:"+fac.getContactoEmisor(), 150, 610, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO:"+fac.getTipoEmisor(), 445, 610, 0);

                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "AREA:"+fac.getArea(), 40, 602, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "E-MAIL:"+fac.getCorreoEmisor(), 150, 602, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TEL:"+fac.getTelefonoEmisor(), 445, 602, 0);

                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CONT. RECEP:"+fac.getContactoReceptor(), 40, 593, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO:"+fac.getTipoReceptor(), 270, 593, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "E-MAIL:"+fac.getCorreoReceptor(), 370, 593, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOTAS:"+fac.getFoliosElectronicos(), 40, 583, 0);
            }
        }
        reporte.finTexto();
        
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        if(factura.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
        {
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
        }
        
        
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            Font font_mini = new Font(Font.FontFamily.HELVETICA, 1, Font.BOLD);
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            PdfPCell celda1=reporte.celda("Cant", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda1.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda1);
            PdfPCell celda2=reporte.celda("U/Med", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda2.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda2);
            PdfPCell celda3=reporte.celda("D E S C R I P C I O N", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda3.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda3);
            PdfPCell celda4=reporte.celda("Precio c/u", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda4.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda4);
            PdfPCell celda5=reporte.celda("T O T A L", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda5.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda5);
            PdfPCell celda6=reporte.celda(" ",font_mini, null, centro, 5, 1,Rectangle.BOTTOM);
            tabla.addCell(celda6);
    }
    
    private void cabeceraNot(PDF reporte, BaseFont bf, PdfPTable tabla, Nota fac) throws DocumentException, IOException
    {
        BaseFont negrita = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.setColorStroke(new GrayColor(0.9f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.roundRectangle(35, 680, 543, 12, 5);//encabezado cliente
        reporte.contenido.roundRectangle(35, 625, 543, 12, 5);//encabezado unidad
        reporte.contenido.roundRectangle(353, 765, 223, 13, 5);//cuadro fecha
        reporte.contenido.fillStroke();
        reporte.contenido.roundRectangle(35, 642, 543, 50, 5);//cuadro cliente
        reporte.contenido.roundRectangle(35, 587, 543, 50, 5);//cuadro unidad
        reporte.contenido.roundRectangle(353, 701, 223, 77, 5);//cuadro C. Emisor

        //Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        
        reporte.inicioTexto();
        switch(nota.getRfcEmisor())
        {
            case "SET0806255W2":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa3666.jpg", 00, -65, 40));
                break;
            case "TBS160622L39":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/tbs.jpg", 00, -65, 40));
                break;
                
            case "PEVV740319ML1":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/torres.jpg", 00, -65, 40));
                break;    
        }
        
        //****************Datos de Emisor
       //****************Datos de Emisor
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getNombreEmisor(), 120, 765, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+fac.getRfcEmisor(), 120, 757, 0);
        
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DOMICILIO FISCAL ", 120, 747, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getCalleEmisor()+" "+fac.getNumeroExteriorEmisor(), 120, 739, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getColoniaEmisor()+", "+fac.getMunicipioEmisor(), 120, 731, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getEstadoEmisor()+", "+fac.getPaisEmisor()+" "+fac.getCpEmisor(), 120, 724, 0);
        
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "LUGAR Y FECHA DE EXPEDICIÓN", 120, 714, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getMunicipioEmisor()+", "+fac.getEstadoEmisor(), 120, 706, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, dateFormat.format(fac.getFecha()), 120, 698, 0);
        
        reporte.contenido.setFontAndSize(negrita, 8);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOTA DE CREDITO", 370, 769, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        if(fac.getFolio()!=null)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getSerie()+"-"+fac.getFolio(), 540, 769, 0);
        else
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getSerieExterno()+"-"+fac.getFolioExterno(), 540, 769, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FOLIO FISCAL: "+fac.getFFiscal(), 360, 755, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FECHA CERTIFICACIÓN: "+fac.getFechaFiscal().substring(0, 19), 360, 746, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "MÉTODO DE PAGO: "+fac.getMetodoPago(), 360, 738, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° CTA. DE PAGO: "+fac.getCuentaPago(), 360, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO DE COMPROBANTE: Egreso (E)", 360, 722, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NÚMERO INTERNO: "+fac.getIdNota(), 360, 714, 0);
        if(fac.getVersion()>3.2)
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "USO CFDI: "+fac.getUsoCfdi().getIdUsoCfdi(), 360, 706, 0);
        
        reporte.contenido.setFontAndSize(bf, 7);
        //************************datos del cliente****************************
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL CLIENTE", 260, 683, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOMBRE: "+fac.getNombreReceptor(), 40, 669, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+fac.getRfcReceptor(), 400, 669, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DOMICILIO FISCAL:    CALLE "+fac.getCalleReceptor()+" "+fac.getNumeroExteriorReceptor()+" COL. "+fac.getColoniaReceptor(), 40, 659, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "MPIO./DEL. "+fac.getMunicipioReceptor()+" ESTADO "+fac.getEstadoReceptor()+" pais "+fac.getPaisReceptor()+" CP "+fac.getCpReceptor(), 120, 649, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE LA UNIDAD", 260, 627, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "MARCA: ", 80, 617, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "TIPO: ", 80, 608, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "NO.SERIE: ", 80, 599, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "ORDEN: ", 80, 590, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "MODELO: ", 350, 617, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "PLACAS: ", 350, 608, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "POLIZA: ", 350, 599, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "KM: ", 350, 590, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "SINIESTRO: ", 490, 617, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "INC: ", 490, 608, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "REPORTE: ", 490, 599, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "COLOR: ", 490, 590, 0);

        if(fac.getOrden()!=null)
        {
            Orden ord=fac.getOrden();
            if(ord.getMarca()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getMarca().getMarcaNombre(), 80, 617, 0);
            if(ord.getTipo()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getTipo().getTipoNombre(), 80, 608, 0);
            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoSerie(), 80, 599, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getIdOrden(), 80, 590, 0);

            if(ord.getModelo()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getModelo(), 350, 617, 0);
            if(ord.getNoPlacas()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoPlacas(), 350, 608, 0);
            if(ord.getPoliza()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getPoliza(), 350, 599, 0);
            if(ord.getKm()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getKm(), 350, 590, 0);

            if(ord.getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getSiniestro(), 490, 617, 0);
            if(ord.getInciso()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getInciso(), 490, 608, 0);
            if(ord.getNoReporte()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoReporte(), 490, 599, 0);
            if(ord.getColor()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getColor(), 490, 590, 0);
        }
        else
        {
            OrdenExterna ord=fac.getOrdenExterna();
            if(ord!=null)
            {
                if(ord.getMarca()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getMarca().getMarcaNombre(), 80, 617, 0);
                if(ord.getTipo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getTipo().getTipoNombre(), 80, 608, 0);

                if(ord.getNoSerie()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoSerie(), 80, 599, 0);
                if(fac.getExtra()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getExtra(), 40, 590, 0);
                    //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getIdOrden(), 80, 550, 0);

                if(ord.getModelo()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ""+ord.getModelo(), 350, 617, 0);
                if(ord.getNoPlacas()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoPlacas(), 350, 608, 0);
                if(ord.getPoliza()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getPoliza(), 350, 599, 0);
                if(ord.getKm()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getKm(), 350, 590, 0);

                if(ord.getSiniestro()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getSiniestro(), 490, 617, 0);
                if(ord.getInciso()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getInciso(), 490, 608, 0);
                if(ord.getNoReporte()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoReporte(), 490, 599, 0);
                if(ord.getColor()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getColor(), 490, 590, 0);
            }
        }
        
        if(fac.getAddenda().compareTo("qualitas")==0)
        {
            reporte.contenido.roundRectangle(35, 570, 543, 12, 5);//encabezado Agente Proveedor
            reporte.contenido.roundRectangle(35, 532, 543, 50, 5);//cuadro Agente Proveedor
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NO.PROV:22194", 40, 561, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CONT. EMISOR:"+fac.getContactoEmisor(), 150, 561, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO:"+fac.getTipoEmisor(), 445, 561, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "AREA:"+fac.getArea(), 40, 553, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "E-MAIL:"+fac.getCorreoEmisor(), 150, 553, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TEL:"+fac.getTelefonoEmisor(), 445, 553, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CONT. RECEP:"+fac.getContactoReceptor(), 40, 544, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO:"+fac.getTipoReceptor(), 270, 544, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "E-MAIL:"+fac.getCorreoReceptor(), 370, 544, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOTAS:"+fac.getFoliosElectronicos(), 40, 535, 0);
        }
        reporte.finTexto();
        
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        
        
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            Font font_mini = new Font(Font.FontFamily.HELVETICA, 1, Font.BOLD);
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            PdfPCell celda1=reporte.celda("Cant", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda1.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda1);
            PdfPCell celda2=reporte.celda("U/Med", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda2.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda2);
            PdfPCell celda3=reporte.celda("D E S C R I P C I O N", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda3.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda3);
            PdfPCell celda4=reporte.celda("Precio c/u", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda4.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda4);
            PdfPCell celda5=reporte.celda("T O T A L", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda5.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda5);
            PdfPCell celda6=reporte.celda(" ",font_mini, null, centro, 5, 1,Rectangle.BOTTOM);
            tabla.addCell(celda6);
    }
    
    private void cabeceraPag(PDF reporte, BaseFont bf, PdfPTable tabla, Pago pag) throws DocumentException, IOException
    {
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        BaseFont negrita = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.setColorStroke(new GrayColor(0.9f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.roundRectangle(35, 680, 543, 12, 5);//encabezado cliente
        /*if(pag.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
            reporte.contenido.roundRectangle(35, 625, 543, 12, 5);//encabezado unidad*/
        reporte.contenido.roundRectangle(353, 765, 223, 13, 5);//cuadro fecha
        reporte.contenido.fillStroke();
        reporte.contenido.roundRectangle(35, 642, 543, 50, 5);//cuadro cliente
        /*if(pag.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")!=0)
            reporte.contenido.roundRectangle(35, 587, 543, 50, 5);//cuadro unidad*/
        reporte.contenido.roundRectangle(353, 701, 223, 77, 5);//cuadro C. Emisor
        
        //Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        
        reporte.inicioTexto();
        switch(pag.getRfcEmisor())
        {
            case "SET0806255W2":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa3666.jpg", 00, -65, 40));
                break;
            case "TBS160622L39":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/tbs.jpg", 00, -65, 40));
                break;
                
            case "PEVV740319ML1":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/torres.jpg", 00, -65, 40));
                break;    
            case "ITP150326MP1":
                reporte.agregaObjeto(reporte.crearImagen("imagenes/imperia.jpg", 00, -65, 40));
                break;
        }
        
        //****************Datos de Emisor
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, pag.getNombreEmisor(), 120, 765, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+pag.getRfcEmisor(), 120, 757, 0);
        
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DOMICILIO FISCAL ", 120, 747, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, pag.getCalleEmisor()+" "+pag.getNumeroExteriorEmisor(), 120, 739, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, pag.getColoniaEmisor()+", "+pag.getMunicipioEmisor(), 120, 731, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, pag.getEstadoEmisor()+", "+pag.getPaisEmisor()+" "+pag.getCpEmisor(), 120, 724, 0);
        
        reporte.contenido.setFontAndSize(negrita, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "LUGAR Y FECHA DE EXPEDICIÓN", 120, 714, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, pag.getMunicipioEmisor()+", "+pag.getEstadoEmisor(), 120, 706, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, dateFormat.format(pag.getFecha()), 120, 698, 0);
        
        reporte.contenido.setFontAndSize(negrita, 8);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CFDI PAGO", 370, 769, 0);
        reporte.contenido.setFontAndSize(bf, 7);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, pag.getSerie()+"-"+pag.getFolio(), 540, 769, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FOLIO FISCAL: "+pag.getFFiscal(), 360, 755, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "FECHA CERTIFICACIÓN: "+pag.getFechaFiscal().substring(0, 19), 360, 746, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO DE COMPROBANTE: Pago (P)", 360, 738, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NÚMERO INTERNO: "+pag.getIdPago(), 360, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "USO CFDI: "+pag.getUsoCfdi().getIdUsoCfdi(), 360, 722, 0);
        
        reporte.contenido.setFontAndSize(bf, 7);
        //************************datos del cliente****************************
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL CLIENTE", 260, 683, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NOMBRE: "+pag.getNombreReceptor(), 40, 669, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+pag.getRfcReceptor(), 400, 669, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DOMICILIO FISCAL:    CALLE "+pag.getCalleReceptor()+" "+pag.getNumeroExteriorReceptor()+" COL. "+pag.getColoniaReceptor(), 40, 659, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "MPIO./DEL. "+pag.getMunicipioReceptor()+" ESTADO "+pag.getEstadoReceptor()+" pais "+pag.getPaisReceptor()+" CP "+pag.getCpReceptor(), 120, 649, 0);
        
        reporte.finTexto();
        
        //agregamos renglones vacios para dejar un espacio
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        
        
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            Font font_mini = new Font(Font.FontFamily.HELVETICA, 1, Font.BOLD);
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            PdfPCell celda1=reporte.celda("Cant", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda1.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda1);
            PdfPCell celda2=reporte.celda("U/Med", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda2.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda2);
            PdfPCell celda3=reporte.celda("D E S C R I P C I O N", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda3.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda3);
            PdfPCell celda4=reporte.celda("Precio c/u", font, contenido, centro, 0, 1, Rectangle.RECTANGLE+Rectangle.TOP);
            celda4.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda4);
            PdfPCell celda5=reporte.celda("T O T A L", font, contenido, centro, 0, 1,Rectangle.RECTANGLE+Rectangle.TOP);
            celda5.setBackgroundColor(new GrayColor(0.9f));
            tabla.addCell(celda5);
            PdfPCell celda6=reporte.celda(" ",font_mini, null, centro, 5, 1,Rectangle.BOTTOM);
            tabla.addCell(celda6);
            
            tabla.addCell(reporte.celda("1.0", font, contenido, derecha, 0,1,12));
            tabla.addCell(reporte.celda("ACT", font, contenido, izquierda, 0,1,12));
            tabla.addCell(reporte.celda("Pago[84111506]", font, contenido, izquierda, 0,1,12));
            tabla.addCell(reporte.celda(""+formatoPorcentaje.format(0.0d), font, contenido, derecha, 0,1,12));
            tabla.addCell(reporte.celda(""+formatoPorcentaje.format(0.0d), font, contenido, derecha, 0,1,12));
    }
    
    public BufferedImage createQR(String data)
    {
        BitMatrix matrix;
        Writer writer = new MultiFormatWriter();
        try {            
            EnumMap<EncodeHintType,String> hints = new EnumMap<EncodeHintType,String>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");            
            matrix = writer.encode(data, BarcodeFormat.QR_CODE, 136, 136, hints);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            byte[] data_array = output.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data_array);
            return ImageIO.read(input);            
        } catch (com.google.zxing.WriterException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
