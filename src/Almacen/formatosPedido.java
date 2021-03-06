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
import Hibernate.entidades.OrdenExterna;
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
public class formatosPedido {
    Herramientas h;
    String sessionPrograma="";
    Usuario usr;
    Almacen miAlmacen;
    int configuracion=1;
    public formatosPedido(Usuario u, String ses, Almacen al, int configuracion)
    {
        this.configuracion=configuracion;
        sessionPrograma=ses;
        usr=u;
        miAlmacen=al;
    }    
    void formato(boolean edo)
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
            OrdenExterna ordEx=null;
            if(mov.length>0)
            {
                if(miAlmacen.getOperacion()==1)
                    ord=mov[0].getPartida().getOrdenByIdOrden();
                if(miAlmacen.getOperacion()==3)
                    ord=miAlmacen.getPedido().getOrden();
            }
            File folder = new File("reportes");
            folder.mkdirs();
            Rectangle pageSize = new Rectangle(PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight()/2); //ancho y alto
            
            if(edo==false)
            {
                reporte.Abrir2(pageSize, "Almacen", "reportes/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
            }
            else
                reporte.Abrir2(PageSize.LETTER, "Almacen", "reportes/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[];
            PdfPTable tabla;
            if(miAlmacen.getOperacion()!=6)
            {
                tam=new float[]{20,20,80,190,20,30,50,50};
                tabla=reporte.crearTabla(8, tam, 100, Element.ALIGN_LEFT);
            }
            else
            {
                tam=new float[]{20,20,80,190,20,30};
                tabla=reporte.crearTabla(6, tam, 100, Element.ALIGN_LEFT);
            }

            cabeceraCompra(reporte, bf, tabla, miAlmacen, ord, edo);
            int ren=0;
            double total=0d;
            if(mov.length>0)
            {
                int renglon=0;
                for(int i=0; i<mov.length; i++)
                {
                    if(miAlmacen.getOperacion()==1)
                    {
                        int r=i+1;
                        renglon ++;
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
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getPartida().getPcp()), font, contenido, derecha, 0,1,12));
                        double sum=mov[i].getCantidad() * mov[i].getPartida().getPcp();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,12));
                        ren++;
                    }
                    if(miAlmacen.getOperacion()==2 || miAlmacen.getOperacion()==6)
                    {
                        int r=i+1;
                        renglon ++;
                        
                        tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,12));
                        if(mov[i].getPartidaExterna().getNoParte()!=null)
                            tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getNoParte(), font, contenido, derecha, 0,1,12));
                        else
                            tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                        tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getDescripcion(), font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getUnidad(), font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,12));
                        if(miAlmacen.getOperacion()==2)
                        {
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getPartidaExterna().getCosto()), font, contenido, derecha, 0,1,12));
                            double sum=mov[i].getCantidad() * mov[i].getPartidaExterna().getCosto();
                            total+=sum;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,12));
                        }
                        if(ren==20)//20
                        {
                            reporte.writer.newPage();
                            reporte.agregaObjeto(tabla);
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(tabla);          
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));

                            tabla=reporte.crearTabla(8, tam, 100, Element.ALIGN_LEFT);
                            cabeceraCompra(reporte, bf, tabla, miAlmacen, ord, edo);
                            ren=-1;
                            renglon=0;
                        }
                        ren++;
                    }
                    if(miAlmacen.getOperacion()==3)
                    {
                        int r=i+1;
                        renglon ++;
                        //tabla.addCell(reporte.celda(""+miAlmacen.getPedido().getPartida().getIdEvaluacion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        //tabla.addCell(reporte.celda(""+miAlmacen.getPedido().getPartida().getSubPartida(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                        if(mov[i].getPartidaExterna().getNoParte()!=null)
                            tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getNoParte(), font, contenido, derecha, 0,1,12));
                        else
                            tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                        //tabla.addCell(reporte.celda(miAlmacen.getPedido().getPartida().getCatalogo().getNombre()+"/"+mov[i].getPartidaExterna().getDescripcion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getDescripcion(), font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(mov[i].getPartidaExterna().getUnidad(), font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,12));
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getPartidaExterna().getCosto()), font, contenido, derecha, 0,1,12));
                        double sum=mov[i].getCantidad() * mov[i].getPartidaExterna().getCosto();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,12));

                        if(ren==20)//20
                        {
                            reporte.writer.newPage();
                            reporte.agregaObjeto(tabla);
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(tabla);          
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));
                            reporte.agregaObjeto(new Paragraph(" "));

                            tabla=reporte.crearTabla(8, tam, 100, Element.ALIGN_LEFT);
                            cabeceraCompra(reporte, bf, tabla, miAlmacen, ord, edo);
                            ren=-1;
                            renglon=0;
                        }
                        ren++;
                    }
                }
                for(renglon=renglon; renglon<20; renglon++)
                {
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                    if(miAlmacen.getOperacion()!=6)
                    {
                        tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,12));
                        tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,12));
                    }
                }
            }
            tabla.addCell(reporte.celda("Nota: ", font, contenido, izquierda, 0,1,3));
            tabla.addCell(reporte.celda(miAlmacen.getNotas(), font, contenido, izquierda, tabla.getNumberOfColumns()-1,1,3));
            
            if(miAlmacen.getOperacion()!=6)
            {
                tabla.addCell(reporte.celda("", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 4,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 7,1,Rectangle.NO_BORDER));
                Configuracion con = (Configuracion)session.get(Configuracion.class, 1);
                double iva=total*con.getIva()/100;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 7,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            }
            else
            {
                tabla.addCell(reporte.celda("", font, contenido, izquierda, 8,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda("", font, contenido, derecha, 8,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda("", font, contenido, derecha, 8,1,Rectangle.RECTANGLE));
            }
            session.beginTransaction().rollback();
            reporte.agregaObjeto(tabla);
            if(edo==true)
            {
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                //reporte.agregaObjeto(new Paragraph(" "));
                //reporte.agregaObjeto(new Paragraph(" "));
                //reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(tabla);
            }
            reporte.cerrar();
            reporte.visualizar2("reportes/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");

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
    
    private void cabeceraCompra(PDF reporte, BaseFont bf, PdfPTable tabla, Almacen almacen, Orden ord, boolean edo)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            //reporte.contenido.setColorFill(new GrayColor(0.9f));
            if(edo==true)
            {
                reporte.contenido.roundRectangle(35, 755, 280, 10, 0);
                reporte.contenido.roundRectangle(35, 735, 280, 20, 0);
            }
            ////*2
                reporte.contenido.roundRectangle(35, 360, 280, 10, 0);
                reporte.contenido.roundRectangle(35, 340, 280, 20, 0);
            ////         
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 13);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);
            reporte.agregaObjeto(reporte.crearImagen("imagenes/"+config.getLogo(), 420, -23, 17));//335
            /*2*/
            if(edo==true)
                reporte.agregarImagen(reporte.crearImagen("imagenes/"+config.getLogo(), 350, -405, 17));//-390//-375 100
            reporte.contenido.setFontAndSize(bf, 12);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            int numAlm=Integer.parseInt(almacen.getAlmacen())+1;
            if(miAlmacen.getTipoMovimiento()==1)
            {
                if(edo==true)
                {
                    if(miAlmacen.getOperacion()==1 || miAlmacen.getOperacion()==2 || miAlmacen.getOperacion()==3)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento en Almacén "+numAlm+" (Entrada de Material): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 767, 0);
                    if(miAlmacen.getOperacion()==5)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de ventas): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 767, 0);
                }
                ////*2
                if(miAlmacen.getOperacion()==1 || miAlmacen.getOperacion()==2 || miAlmacen.getOperacion()==3)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento en Almacén "+numAlm+" (Entrada de Material): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 372, 0);
                if(miAlmacen.getOperacion()==6)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Devolución de ventas): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 372, 0);
                /////
            }
            else
            {
                if(edo==true)
                {
                    if(miAlmacen.getOperacion()==1 || miAlmacen.getOperacion()==2 || miAlmacen.getOperacion()==3)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento de Almacén "+numAlm+" (Devolución de material a proveedor): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 767, 0);
                    if(miAlmacen.getOperacion()==6)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Entrega de ventas): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 767, 0);
                }
                ///**2
                if(miAlmacen.getOperacion()==1 || miAlmacen.getOperacion()==2 || miAlmacen.getOperacion()==3)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento de Almacén "+numAlm+" (Devolución de material a proveedor): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 372, 0);
                if(miAlmacen.getOperacion()==6)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimientos de Almacén "+numAlm+" (Entrega de ventas): "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 372, 0);
                ///
            }

            reporte.contenido.setFontAndSize(bf, 7);
            if(edo==true)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 306, 757, 0);
            /*2*/
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 306, 362, 0);        
            if(ord!=null)
                ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 

            //************************datos de movimiento****************************
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
            if(edo==true)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Materiales y Refacciones del Almacén", 40, 757 , 0);
            /*2*/
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Materiales y Refacciones del Almacén", 40, 362 , 0);
            if(edo==true)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Movimiento: "+almacen.getIdAlmacen(), 40, 747, 0);
            /*2*/
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Movimiento: "+almacen.getIdAlmacen(), 40, 352, 0);
            
            if(edo==true)
            {
                if(almacen.getTipoMovimiento()==1)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Entrada", 120, 747, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Salida", 120, 747, 0);
            }
            ////2
            if(almacen.getTipoMovimiento()==1)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Entrada", 120, 352, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Movimiento : Salida", 120, 352, 0);
            ////
            if(edo==true)
            {
                if(almacen.getOperacion()==1)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Pedido", 220, 747, 0);
                if(almacen.getOperacion()==2)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Pedido E.", 220, 747, 0);
                if(almacen.getOperacion()==3)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Pedido A.", 220, 747, 0);
                if(almacen.getOperacion()==6)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Venta", 220, 747, 0);
            }
            ////2
            if(almacen.getOperacion()==1)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Pedido", 220, 352, 0);
            if(almacen.getOperacion()==2)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Pedido E.", 220, 352, 0);
            if(almacen.getOperacion()==3)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Pedido A.", 220, 352, 0);
            if(almacen.getOperacion()==6)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo de Operación : Venta", 220, 352, 0);
            ////
            if(ord!=null)
            {
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Orden: "+ord.getIdOrden(), 40, 737, 0);
                /*2*/
                if(edo==true)
                {
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N°. Orden: "+ord.getIdOrden(), 40, 342, 0);
                }
            }

            //Firmas de material 
            if(edo==true)
            {
                if(miAlmacen.getTipoMovimiento()==1)
                {
                    reporte.contenido.roundRectangle(45, 425, 130, 1, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getEntrego(), 45, 415, 0);
                    reporte.contenido.roundRectangle(250, 425, 130, 1, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 415, 0);
                    reporte.contenido.roundRectangle(440, 425, 130, 1, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getUsuario().getEmpleado().getNombre(), 440, 415, 0);
                }
                else
                {
                    reporte.contenido.roundRectangle(45, 425, 130, 1, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getUsuario().getEmpleado().getNombre(), 45, 415, 0);
                    reporte.contenido.roundRectangle(250, 425, 130, 1, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 415, 0);
                    reporte.contenido.roundRectangle(440, 425, 130, 1, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getEntrego(), 440, 415, 0);
                }
            }

            ///2
            if(miAlmacen.getTipoMovimiento()==1)
            {
                reporte.contenido.roundRectangle(45, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ENTREGA: "+almacen.getEntrego(), 45, 20, 0);
                reporte.contenido.roundRectangle(250, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "REVISO: ", 250, 20, 0);
                reporte.contenido.roundRectangle(440, 30, 130, 1, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RECIBE: "+almacen.getUsuario().getEmpleado().getNombre(), 440, 20, 0);
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
            //agregamos renglones vacios para dejar un espacio(tabla)
            Paragraph pg=new Paragraph(" ");
            pg.setSpacingAfter(5f);
            reporte.agregaObjeto(pg);
            reporte.contenido.setFontAndSize(bf, 12);

            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor cabecera=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;

            tabla.addCell(reporte.celda("N°", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("N° Parte", font, cabecera, centro, 0, 1,Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            if(tabla.getNumberOfColumns()>6)
            {
                tabla.addCell(reporte.celda("Costo c/u", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
                tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.BOTTOM));
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
}