/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Almacen;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
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
public class formatosAlmacen {
    
    Herramientas h;
    String sessionPrograma="";
    Usuario usr;
    Almacen miAlmacen;
    public formatosAlmacen(Usuario u, String ses, Almacen al)
    {
        sessionPrograma=ses;
        usr=u;
        miAlmacen=al;
    }
    
    void formato()
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
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

                PDF reporte = new PDF();
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                Movimiento[] mov=(Movimiento[]) session.createCriteria(Movimiento.class).add(Restrictions.eq("almacen.idAlmacen", miAlmacen.getIdAlmacen())).list().toArray(new Movimiento[0]);
                Orden ord=null;
                if(mov.length>0)
                    ord=mov[0].getPartida().getOrdenByIdOrden();
                //File folder = new File("reportes/"+ord.getIdOrden());
                //folder.mkdirs();
                reporte.Abrir(PageSize.LETTER, "Almacen", "reportes/"+ord.getIdOrden()+"/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");
                Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;
                float tam[]=new float[]{20,20,80,190,20,30,50,50};
                PdfPTable tabla=reporte.crearTabla(8, tam, 100, Element.ALIGN_LEFT);

                cabeceraCompra(reporte, bf, tabla, miAlmacen, ord);

                int ren=0;
                double total=0d;
                if(mov.length>0)
                {
                    for(int i=0; i<mov.length; i++)
                    {
                        int r=i+1;
                        //N°
                        tabla.addCell(reporte.celda(""+mov[i].getPartida().getIdEvaluacion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        //#
                        tabla.addCell(reporte.celda(""+mov[i].getPartida().getSubPartida(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(mov[i].getPartida().getEjemplar()!=null)
                        {
                            //No de parte
                            tabla.addCell(reporte.celda(""+mov[i].getPartida().getEjemplar().getIdParte(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        }
                        else
                        tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //Descripcion
                        tabla.addCell(reporte.celda(mov[i].getPartida().getCatalogo().getNombre(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                        //med
                        tabla.addCell(reporte.celda(mov[i].getPartida().getMed(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        
                        //cant 
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getCantidad()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(mov[i].getPartida().getPcp()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        double sum=mov[i].getCantidad() * mov[i].getPartida().getPcp();
                        total+=sum;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                        if(ren==38)
                        {
                            reporte.agregaObjeto(tabla);
                            reporte.writer.newPage();
                            tabla=reporte.crearTabla(8, tam, 100, Element.ALIGN_LEFT);
                            cabeceraCompra(reporte, bf, tabla, miAlmacen, ord);
                            ren=-1;
                        }
                        ren++;
                    }
                }
                tabla.addCell(reporte.celda("Notas:", font, contenido, izquierda, 0,1,Rectangle.BOTTOM));
                tabla.addCell(reporte.celda(miAlmacen.getNotas(), font, contenido, izquierda, 7,1,Rectangle.BOTTOM));
                if(miAlmacen.getTipoMovimiento()==1)
                    tabla.addCell(reporte.celda("Entrego: "+miAlmacen.getEntrego(), font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                else
                    tabla.addCell(reporte.celda("recibió:"+miAlmacen.getEntrego(), font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 4,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("IVA:", font, contenido, derecha, 7,1,Rectangle.NO_BORDER));
                double iva=total*0.16d;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total:", font, contenido, derecha, 7,1,Rectangle.NO_BORDER));
                total+=iva;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                session.beginTransaction().rollback();

                reporte.agregaObjeto(tabla);
                reporte.cerrar();
                reporte.visualizar("reportes/"+ord.getIdOrden()+"/"+valor+"-"+miAlmacen.getIdAlmacen()+"-almacen.pdf");

            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
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
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            if(almacen.getPedido()!=null)
            {
                reporte.contenido.roundRectangle(35, 695, 180, 10, 0);
                reporte.contenido.roundRectangle(35, 625, 180, 80, 0);

                reporte.contenido.roundRectangle(215, 695, 180, 10, 0);
                reporte.contenido.roundRectangle(215, 625, 180, 80, 0);
            }
            reporte.contenido.roundRectangle(395, 695, 180, 10, 0);
            reporte.contenido.roundRectangle(395, 625, 180, 80, 0);

            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 13);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.agregaObjeto(reporte.crearImagen("imagenes/grande300115.jpg", 30, -40, 60));
            reporte.contenido.setFontAndSize(bf, 12);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            if(almacen.getPedido()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento en almacen: "+almacen.getIdAlmacen()+ " Pedido:"+almacen.getPedido().getIdPedido(), 35, 710, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimiento en almacen: "+almacen.getIdAlmacen(), 35, 710, 0);
            reporte.contenido.setFontAndSize(bf, 7);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 710, 0);

            ord = (Orden)session.get(Orden.class, ord.getIdOrden()); 

            //************************datos del proveedor****************************
            if(almacen.getPedido()!=null)
            {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DEL PROVEEDOR", 73, 697, 0);

                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, almacen.getPedido().getProveedorByIdProveedor().getNombre(), 40, 687, 0);

                if(almacen.getPedido().getProveedorByIdProveedor().getDireccion()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, almacen.getPedido().getProveedorByIdProveedor().getDireccion(), 40, 677, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 40, 677, 0);

                if(almacen.getPedido().getProveedorByIdProveedor().getColonia()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+almacen.getPedido().getProveedorByIdProveedor().getColonia(), 40, 667, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 40, 667, 0);

                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Edo: "+almacen.getPedido().getProveedorByIdProveedor().getEstado(), 40, 657, 0);

                if(almacen.getPedido().getProveedorByIdProveedor().getTel1()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel: "+almacen.getPedido().getProveedorByIdProveedor().getTel1(), 40, 647, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tel:", 40, 647, 0);

                if(almacen.getPedido().getProveedorByIdProveedor().getTel1()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto: "+almacen.getPedido().getProveedorByIdProveedor().getRepresentante(), 40, 637, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cto:", 40, 637, 0);

                if(almacen.getPedido().getProveedorByIdProveedor().getEmail()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: "+almacen.getPedido().getProveedorByIdProveedor().getEmail(), 40, 627, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Email: ", 40, 627, 0);


            //**********************datos de facturacion*****************************
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS DE FACTURACIÓN", 250, 697, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, almacen.getPedido().getProveedorByIdEmpresa().getNombre(), 220, 687, 0);
                if(almacen.getPedido().getProveedorByIdEmpresa().getDireccion()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, almacen.getPedido().getProveedorByIdEmpresa().getDireccion(), 220, 677, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dir:", 220, 677, 0);
                if(almacen.getPedido().getProveedorByIdEmpresa().getColonia()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col: "+almacen.getPedido().getProveedorByIdEmpresa().getColonia(), 220, 667, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col:", 220, 667, 0);
                if(almacen.getPedido().getProveedorByIdEmpresa().getPoblacion()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob: "+almacen.getPedido().getProveedorByIdEmpresa().getPoblacion(), 220, 657, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pob:", 220, 657, 0);
                if(almacen.getPedido().getProveedorByIdEmpresa().getCp()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP: "+almacen.getPedido().getProveedorByIdEmpresa().getCp(), 220, 647, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CP:", 220, 647, 0);
                if(almacen.getPedido().getProveedorByIdEmpresa().getRfc()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC: "+almacen.getPedido().getProveedorByIdEmpresa().getRfc(), 220, 637, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "RFC:", 220, 537, 0);
            }
            //**********************datos de la unidad*****************************
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATOS LA UNIDAD", 450, 697, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Orden: "+ord.getIdOrden(), 410, 687, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tipo: "+ord.getTipo().getTipoNombre(), 410, 677, 0);
            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Serie: "+ord.getNoSerie(), 410, 667, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Serie: ", 410, 667, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Modelo: "+ord.getModelo(), 410, 657, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañia: "+ord.getCompania().getNombre(), 410, 647, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cli:"+ord.getClientes().getNombre(), 410, 637, 0);
            if(ord.getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:"+ord.getSiniestro(), 410, 627, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Siniestro:", 410, 627, 0);


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
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));

                Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
                BaseColor cabecera=BaseColor.GRAY;
                int centro=Element.ALIGN_CENTER;

                tabla.addCell(reporte.celda("N°", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("N° Parte", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Costo c/u", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));

            reporte.contenido.roundRectangle(50, 40, 180, 1, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Almacen", 120, 30, 0);
            reporte.contenido.roundRectangle(370, 40, 180, 1, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Proveedor", 440, 30, 0);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
}
