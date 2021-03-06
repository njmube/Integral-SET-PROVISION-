/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Almacen;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Orden;
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
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import Integral.Herramientas;
import Integral.PDF;
/**
 *
 * @author salvador
 */
public class formatosOrden {
    Herramientas h;
    String sessionPrograma="";
    Usuario usr;
    Almacen miAlmacen;
    int configuracion=1;
    public formatosOrden(Usuario u, String ses, Almacen al, int configuracion)
    {
        this.configuracion=configuracion;
        sessionPrograma=ses;
        usr=u;
        miAlmacen=al;
    }
    
    void formato(boolean op)
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            miAlmacen=(Almacen)session.get(Almacen.class, miAlmacen.getIdAlmacen());
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.MACROMAN, BaseFont.EMBEDDED);//NOT_EMBEDDED
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            Movimiento[] mov=(Movimiento[]) session.createCriteria(Movimiento.class).add(Restrictions.eq("almacen.idAlmacen", miAlmacen.getIdAlmacen())).list().toArray(new Movimiento[0]);
            Orden ord=null;
            if(mov.length>0)
            {
                if(mov[0].getPartida()!=null)
                {
                    ord=mov[0].getPartida().getOrdenByIdOrden();
                }
                else
                {
                    if(miAlmacen.getOperacion()==8)
                    {
                        ord=miAlmacen.getOrden();
                    }
                    else
                    {
                        if(miAlmacen.getOperacion()!=9)
                            ord=mov[0].getPartidaExterna().getPedido().getOrden();
                    }
                    //ord=miAlmacen.getPedido().getPartida().getOrdenByIdOrden();
                    //ord=mov[0].getOrden();
                }
            }
            String complemento="";
            if(ord!=null)
                complemento="/"+ord.getIdOrden();
            File folder = new File("reportes"+complemento);
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "Almacen", "reportes"+complemento+"/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor cabecera=BaseColor.WHITE;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{20,20,80,190,20,30};
            PdfPTable tabla=reporte.crearTabla(6, tam, 100, Element.ALIGN_LEFT);
            
            tabla.addCell(reporte.celda("N°", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("N° Parte", font, cabecera, centro, 0, 1,Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            
            cabeceraCompra(reporte, bf, tabla, miAlmacen, ord);
            int ren=0;
            double total=0d;
            if(mov.length>0)
            {
                int renglon=0;
                for(int i=0; i<mov.length; i++)
                {
                    int r=i+1;
                    renglon ++;
                    if(mov[i].getPartida()!=null)
                    {
                        tabla.addCell(reporte.celda(""+mov[i].getPartida().getIdEvaluacion(), font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(""+mov[i].getPartida().getSubPartida(), font, contenido, derecha, 0,1,12));
                        if(mov[i].getPartida().getEjemplar()!=null)
                            tabla.addCell(reporte.celda(""+mov[i].getPartida().getEjemplar().getIdParte(), font, contenido, derecha, 0,1,12));
                        else
                            tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                        String anotacion="";
                        if(mov[i].getPartida().getInstruccion()!=null)
                            anotacion=mov[i].getPartida().getInstruccion();
                        tabla.addCell(reporte.celda(mov[i].getPartida().getCatalogo().getNombre()+" "+anotacion, font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(mov[i].getPartida().getMed(), font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,12));
                    }
                    else
                    {
                        if(miAlmacen.getOperacion()==8 || miAlmacen.getOperacion()==9)
                        {
                            tabla.addCell(reporte.celda("-", font, contenido, izquierda, 0,1,12));
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,12));
                            tabla.addCell(reporte.celda(mov[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,12));
                            tabla.addCell(reporte.celda(mov[i].getEjemplar().getCatalogo().getNombre(), font, contenido, izquierda, 0,1,12));
                            tabla.addCell(reporte.celda(mov[i].getEjemplar().getMedida(), font, contenido, izquierda, 0,1,12));
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,12));
                        }
                        else
                        {
                            tabla.addCell(reporte.celda("-", font, contenido, izquierda, 0,1,12));
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,12));
                            if(mov[i].getPartidaExterna().getEjemplar()!=null)
                                tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getEjemplar().getIdParte(), font, contenido, derecha, 0,1,12));
                            else
                            {
                                if(mov[i].getPartidaExterna().getNoParte()!=null)
                                    tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getNoParte(), font, contenido, derecha, 0,1,12));
                                else
                                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                            }
                            tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getDescripcion(), font, contenido, izquierda, 0,1,12));
                            tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getUnidad(), font, contenido, izquierda, 0,1,12));
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,12));
                        }
                    }
                    ren++;
                }
                for(renglon=renglon; renglon<19; renglon++)
                {
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                }
            }
            tabla.addCell(reporte.celda("Notas: ", font, contenido, izquierda, 0,1,3));
            tabla.addCell(reporte.celda(miAlmacen.getNotas(), font, contenido, izquierda, 7,1,3));
            session.beginTransaction().rollback();

            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
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
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar2("reportes"+complemento+"/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    
    void formato1()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            miAlmacen=(Almacen)session.get(Almacen.class, miAlmacen.getIdAlmacen());
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.MACROMAN, BaseFont.EMBEDDED);//NOT_EMBEDDED
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            Movimiento[] mov=(Movimiento[]) session.createCriteria(Movimiento.class).add(Restrictions.eq("almacen.idAlmacen", miAlmacen.getIdAlmacen())).list().toArray(new Movimiento[0]);
            Orden ord=null;
            if(mov.length>0)
            {
                if(mov[0].getPartida()!=null)
                {
                    ord=mov[0].getPartida().getOrdenByIdOrden();
                }
                else
                {
                    if(miAlmacen.getOperacion()==8)
                    {
                        ord=miAlmacen.getOrden();
                    }
                    else
                    {
                        if(miAlmacen.getOperacion()!=9)
                        {
                            if(mov[0].getPartidaExterna().getPedido()!=null)
                                ord=mov[0].getPartidaExterna().getPedido().getOrden();
                            else
                                ord=null;
                        }
                    }
                    //ord=miAlmacen.getPedido().getPartida().getOrdenByIdOrden();
                    //ord=mov[0].getOrden();
                }
            }
            String complemento="";
            if(ord!=null)
                complemento="/"+ord.getIdOrden();
            File folder = new File("reportes"+complemento);
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "Almacen", "reportes"+complemento+"/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            BaseColor cabecera=BaseColor.GRAY;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            
            float[] tam=new float[]{20,20,80,190,20,30};//,50,50
            PdfPTable tabla=reporte.crearTabla(6, tam, 100, Element.ALIGN_LEFT);//8
            tabla.addCell(reporte.celda("N°", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("N° Parte", font, cabecera, centro, 0, 1,Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            //tabla.addCell(reporte.celda("Costo c/u", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            //tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            
            cabeceraCompra(reporte, bf, tabla, miAlmacen, ord);
            
            int ren=0;
            double total=0d;
            if(mov.length>0)
            {
                int renglon=0;
                for(int i=0; i<mov.length; i++)
                {
                    int r=i+1;
                    renglon++;
                    tabla.addCell(reporte.celda("-", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(mov[i].getEjemplar().getIdParte(), font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(mov[i].getEjemplar().getComentario(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(mov[i].getEjemplar().getMedida(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,12));
                    //tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getValor()), font, contenido, derecha, 0,1,12));
                    //double sum=mov[i].getCantidad() * mov[i].getValor();
                    //total+=sum;
                    //tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,12));
                    ren++;
                }
                for(renglon=renglon; renglon<19; renglon++)
                {
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    //tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    //tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                }
            }
            tabla.addCell(reporte.celda("Notas: ", font, contenido, izquierda, 0,1,Rectangle.BOTTOM));
            tabla.addCell(reporte.celda(miAlmacen.getNotas(), font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
            
            /*tabla.addCell(reporte.celda("", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 4,1,Rectangle.NO_BORDER));
            tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 7,1,Rectangle.NO_BORDER));
            Configuracion con = (Configuracion)session.get(Configuracion.class, 1);
            double iva=total*con.getIva()/100;
            tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 7,1,Rectangle.NO_BORDER));
            total+=iva;
            tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));*/
                
            session.beginTransaction().rollback();

            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            //reporte.agregaObjeto(new Paragraph(" "));
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
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar2("reportes"+complemento+"/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    
    private void cabeceraCompra(PDF reporte, BaseFont bf, PdfPTable tabla, Almacen almacen, Orden ord)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            //reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(35, 755, 280, 10, 0);
            reporte.contenido.roundRectangle(35, 735, 280, 20, 0);
            ////*2
            reporte.contenido.roundRectangle(35, 358, 280, 10, 0);
            reporte.contenido.roundRectangle(35, 338, 280, 20, 0);
            ////         
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 13);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);
            reporte.agregaObjeto(reporte.crearImagen("imagenes/"+config.getLogo(), 345, -23, 20));
            /*2*/reporte.agregarImagen(reporte.crearImagen("imagenes/"+config.getLogo(), 345, -420, 20));
            reporte.contenido.setFontAndSize(bf, 12);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            int numAlm=Integer.parseInt(almacen.getAlmacen())+1;
            if(miAlmacen.getTipoMovimiento()==1)
            {
                if(miAlmacen.getOperacion()==4)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos en Almacén "+numAlm+" (Entrada de Material): "+almacen.getIdAlmacen(), 35, 767, 0);
                if(miAlmacen.getOperacion()==5)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de material de operarios): "+almacen.getIdAlmacen(), 35, 767, 0);
                if(miAlmacen.getOperacion()==8)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de consumible): "+almacen.getIdAlmacen(), 35, 767 , 0);            
                //*********2
                if(miAlmacen.getOperacion()==4)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos en Almacén "+numAlm+" (Entrada de Material): "+almacen.getIdAlmacen(), 35, 370, 0);                
                if(miAlmacen.getOperacion()==5)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de material de operarios): "+almacen.getIdAlmacen(), 35, 370 , 0);            
                if(miAlmacen.getOperacion()==8)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de consumible): "+almacen.getIdAlmacen(), 35, 370 , 0);            
                ////
            }
            else
            {
                if(miAlmacen.getOperacion()==4)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de material a proveedor): "+almacen.getIdAlmacen(), 35, 767, 0);
                if(miAlmacen.getOperacion()==5)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Entrega de material a operarios): "+almacen.getIdAlmacen(), 35, 767, 0);
                if(miAlmacen.getOperacion()==8)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Entrega de consumible): "+almacen.getIdAlmacen(), 35, 767, 0);
                //*********2
                if(miAlmacen.getOperacion()==4)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento de Almacén "+numAlm+" (Devolución de material a proveedor): "+almacen.getIdAlmacen(), 35, 370, 0);
                if(miAlmacen.getOperacion()==5)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Entrega de material a operarios): "+almacen.getIdAlmacen(), 35, 370, 0);
                if(miAlmacen.getOperacion()==8)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Entrega de consumible): "+almacen.getIdAlmacen(), 35, 370, 0);
                ///
            }

            reporte.contenido.setFontAndSize(bf, 7);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 306, 757, 0);
            /*2*/reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 306, 360, 0);

            if(ord!=null)
                ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 

            //************************datos de movimiento****************************
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Materiales y Refacciones del Almacén", 40, 757 , 0);
            /*2*/reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Materiales y Refacciones del Almacén", 40, 360 , 0); 
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Movimiento: "+almacen.getIdAlmacen(), 40, 747, 0);
            /*2*/ reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Movimiento: "+almacen.getIdAlmacen(), 40, 350, 0);
            if(almacen.getTipoMovimiento()==1)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Entrada", 120, 747, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Salida", 120, 747, 0);
            ////**2
            if(almacen.getTipoMovimiento()==1)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Entrada", 120, 350, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Salida", 120, 350, 0);
            ////

            if(almacen.getOperacion()==4)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Operación : Compañía", 220, 747, 0);
            if(almacen.getOperacion()==5)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Operación : Operarios", 220, 747, 0);
            if(almacen.getOperacion()==8)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Operación : Consumibles", 220, 747, 0);
            
            ////**2
            if(almacen.getOperacion()==4)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Operación : Compañía", 220, 350, 0);
            if(almacen.getOperacion()==5)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Operación : Operarios", 220, 350, 0);
            if(almacen.getOperacion()==8)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Operación : Consumibles", 220, 350, 0);
            ////

            if(ord!=null)
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Orden: "+ord.getIdOrden(), 40, 737, 0);
                /*2*/ reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Orden: "+ord.getIdOrden(), 40, 340, 0);
            }

            //Firmas de material 
            if(miAlmacen.getTipoMovimiento()==1)
            {
                reporte.contenido.roundRectangle(45, 450, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getEntrego(), 45, 440, 0);
                reporte.contenido.roundRectangle(250, 450, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 440, 0);
                reporte.contenido.roundRectangle(440, 450, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getUsuario().getEmpleado().getNombre(), 440, 440, 0);
            }
            else
            {
                reporte.contenido.roundRectangle(45, 450, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getUsuario().getEmpleado().getNombre(), 45, 440, 0);
                reporte.contenido.roundRectangle(250, 450, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 440, 0);
                reporte.contenido.roundRectangle(440, 450, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getEntrego(), 440, 440, 0);
            }

            /*2*/if(miAlmacen.getTipoMovimiento()==1)
            {
                reporte.contenido.roundRectangle(45, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getEntrego(), 45, 20, 0);
                reporte.contenido.roundRectangle(250, 30, 130,  1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 20, 0);
                reporte.contenido.roundRectangle(440, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getUsuario().getEmpleado().getNombre(), 440,  20, 0);
            }
            else
            {
                reporte.contenido.roundRectangle(45, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getUsuario().getEmpleado().getNombre(), 45, 20, 0);
                reporte.contenido.roundRectangle(250, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 20, 0);
                reporte.contenido.roundRectangle(440, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getEntrego(), 440, 20, 0);
            }
            ///
            reporte.finTexto();
            reporte.contenido.setFontAndSize(bf, 12);
            //agregamos renglones vacios para dejar un espacio(tabla)
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));

            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            int centro=Element.ALIGN_CENTER;
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
}