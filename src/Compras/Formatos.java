/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Adicionales;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author salvador
 */
public class Formatos {
    
    private Session session;
    Herramientas h;
    String sessionPrograma="";
    Usuario usr;
    public Orden ord;
    public Factura factura=null;
    String no_ped="";
    public Formatos(Usuario u, String ses, Orden o, String p)
    {
        sessionPrograma=ses;
        usr=u;
        ord=o;
        no_ped=p;
    }
    
    public Formatos(Usuario u, String ses, Orden o)
    {
        sessionPrograma=ses;
        usr=u;
        ord=o;
    }

    public void prefactura()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        ord=(Orden)session.get(Orden.class, ord.getIdOrden());
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
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
            Configuracion con=(Configuracion)session.get(Configuracion.class, 1);
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
            if(/*ped.getUsuarioByAutorizo()!=null &&*/ ped.getUsuarioByAutorizo2()!=null)
            {
                reporte.estatusAutoriza(/*ped.getUsuarioByAutorizo().getEmpleado().getNombre()*/"", ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
            }
            else
                reporte.estatusAutoriza("","       NO AUTORIZADO");
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
            tabla.addCell(reporte.celda("[Los montos estan en Pesos]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
            tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 5,1,Rectangle.NO_BORDER));
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

                if(/*ped.getUsuarioByAutorizo()!=null &&*/ ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(/*ped.getUsuarioByAutorizo().getEmpleado().getNombre()*/"", ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("","       NO AUTORIZADO");
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
                tabla.addCell(reporte.celda("[Los montos estan en Pesos]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda("Sub-total:", font, contenido, derecha, 5,1,Rectangle.NO_BORDER));
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
                if(/*ped.getUsuarioByAutorizo()!=null &&*/ ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(/*ped.getUsuarioByAutorizo().getEmpleado().getNombre()*/"", ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("","       NO AUTORIZADO");
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
                tabla.addCell(reporte.celda("[Los montos estan en Pesos]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
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
                reporte.estatusAutoriza("","");
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
                tabla.addCell(reporte.celda("[Los montos estan en Pesos]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
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
                
                reporte.estatusAutoriza("","");
                if(/*ped.getUsuarioByAutorizo()!=null &&*/ ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(/*ped.getUsuarioByAutorizo().getEmpleado().getNombre()*/"", ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("","       NO AUTORIZADO");
                
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
                tabla.addCell(reporte.celda("[Los montos estan en Pesos]", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
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
                
                if(/*ped.getUsuarioByAutorizo()!=null &&*/ ped.getUsuarioByAutorizo2()!=null)
                {
                    reporte.estatusAutoriza(/*ped.getUsuarioByAutorizo().getEmpleado().getNombre()*/"", ped.getUsuarioByAutorizo2().getEmpleado().getNombre());
                }
                else
                    reporte.estatusAutoriza("","       NO AUTORIZADO");
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
                tabla.addCell(reporte.celda("[Los montos estan en Pesos]", font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
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

        Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
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
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "2da. De la Cadena S/N", 110, 760, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col. Reforma", 110, 750, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "San Pedro Totoltepec", 110, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Toluca, Méx.   C.P. 50200", 110, 730, 0);
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
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "2da. De la Cadena S/N", 110, 760, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Col. Reforma", 110, 750, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "San Pedro Totoltepec", 110, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Toluca, Méx.   C.P. 50200", 110, 730, 0);
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

        Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        
        reporte.inicioTexto();
        reporte.agregaObjeto(reporte.crearImagen("imagenes/factura300115.jpg", 00, -32, 40));
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
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "2a. DE LA CADENA S/N COL. SAN PEDRO TOTOLTEPEC MEXICO CP 50200", 40, 702, 0);
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
        //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "asegurado: ", 80, 620, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo: ", 350, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Placas: ", 350, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Poliza: ", 350, 630, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Km: ", 350, 620, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Sinisestro: ", 490, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "INC: ", 490, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Reporte: ", 490, 630, 0);

        if(factura==null)
        {
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getMarca().getMarcaNombre(), 80, 650, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getTipo().getTipoNombre(), 80, 640, 0);
            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getNoSerie(), 80, 630, 0);
            
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
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, ord.getKm(), 365, 620, 0);
            
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
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File(ruta+"xml-timbrados/");
            folder.mkdirs();
            String fi="xml-timbrados/"+factura.getRfcEmisor()+"_"+factura.getSerie()+"_"+factura.getFolio()+"_"+factura.getRfcReceptor()+".pdf";
            reporte.Abrir(PageSize.LETTER, "Pedido", "xml-timbrados/"+factura.getRfcEmisor()+"_"+factura.getSerie()+"_"+factura.getFolio()+"_"+factura.getRfcReceptor()+".pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{40,40,350,70,70};
            PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);

            this.cabeceraFac(reporte, bf, tabla, factura);

            Concepto[] concepto = (Concepto[])session.createCriteria(Concepto.class).add(Restrictions.eq("factura.idFactura", factura.getIdFactura())).addOrder(Order.asc("idConcepto")).list().toArray(new Concepto[0]);

            int ren=0;
            double total=0.0;
            if(concepto.length>0)
            {
                for(int i=0; i<concepto.length; i++)
                {
                    tabla.addCell(reporte.celda(""+concepto[i].getCantidad(), font, contenido, derecha, 0,1,12));
                    tabla.addCell(reporte.celda(concepto[i].getMedida(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(concepto[i].getDescripcion().toUpperCase(), font, contenido, izquierda, 0,1,12));
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(concepto[i].getPrecio()), font, contenido, derecha, 0,1,12));
                    double tot=concepto[i].getPrecio()*concepto[i].getCantidad();
                    total+=tot;
                    tabla.addCell(reporte.celda(""+formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,12));
                }
            }
            PdfPTable tabla1=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
            tabla1.addCell(reporte.celda("Metodo de Pago:"+factura.getMetodoPago(), font, contenido, izquierda, 3,1,Rectangle.TOP));
            tabla1.addCell(reporte.celda("SUB-TOTAL:", font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(total), font, contenido, derecha, 0,1,Rectangle.TOP+Rectangle.BOTTOM+12));
            tabla1.addCell(reporte.celda("Lugar de Expedición: "+factura.getMunicipioEmisor()+", "+factura.getEstadoEmisor(), font, contenido, izquierda, 3,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("IVA:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            Configuracion con=(Configuracion)session.get(Configuracion.class, 1);
            double iva=total*(con.getIva()*0.01);
            tabla1.addCell(reporte.celda(formatoPorcentaje.format(iva), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda("(CANTIDAD CON LETRA)", font, contenido, izquierda, 3,2,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda("DEDUCIBLE:", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla1.addCell(reporte.celda(""+factura.getDeducible(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
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
            reporte.visualizar(fi);

        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }
    
    private void cabeceraFac(PDF reporte, BaseFont bf, PdfPTable tabla, Factura fac)
    {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(35, 660, 543, 40, 5);//cuadro cliente
        reporte.contenido.roundRectangle(35, 618, 543, 40, 5);//cuadro unidad
        
        reporte.contenido.roundRectangle(353, 738, 223, 10, 0);//cuadro fecha
        reporte.contenido.roundRectangle(353, 728, 223, 10, 0);//cuadro F.Fiscal
        reporte.contenido.roundRectangle(353, 718, 223, 10, 0);//cuadro C. SAT
        reporte.contenido.roundRectangle(353, 708, 223, 10, 0);//cuadro C. Emisor

        Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        
        reporte.inicioTexto();
        reporte.agregaObjeto(reporte.crearImagen("imagenes/factura300115.jpg", 00, -32, 40));
        reporte.contenido.setFontAndSize(bf, 10);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "FACTURA", 520, 765, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, fac.getFolio(), 520, 755, 0);
        reporte.contenido.setFontAndSize(bf, 8);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:", 425, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getFechaFiscal()/*new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())*/, 430, 740, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Folio Fiscal:", 425, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT,fac.getFFiscal(), 430, 730, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Certificado SAT:", 425, 720, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Certificado Emisor:", 425, 710, 0);
        reporte.contenido.setFontAndSize(bf, 6);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "2a. DE LA CADENA S/N COL. SAN PEDRO TOTOLTEPEC MEXICO CP 50200", 40, 702, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Tel. (01 722) 199-24- 04", 570, 702, 0);
        
        reporte.contenido.setFontAndSize(bf, 8);
        //************************datos del cliente****************************
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Cliente: "+fac.getNombreReceptor(), 80, 692, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getNombreReceptor(), 85, 692, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Direccion: ", 80, 682, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, fac.getCalleReceptor()+" "+fac.getNumeroExteriorReceptor()+" Col:"+fac.getColoniaReceptor(), 85, 682, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Ciudad: ", 80, 672, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, fac.getMunicipioReceptor()+" "+fac.getMunicipioReceptor(), 85, 672, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "R.F.C.: ", 80, 662, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, fac.getRfcReceptor(), 85, 662, 0);
        
        //**********************datos de la unidad*****************************
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Marca: ", 80, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Tipo: ", 80, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "No. Serie: ", 80, 630, 0);
        //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "asegurado: ", 80, 620, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo: ", 350, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Placas: ", 350, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Poliza: ", 350, 630, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Km: ", 350, 620, 0);
        
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Sinisestro: ", 490, 650, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "INC: ", 490, 640, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Reporte: ", 490, 630, 0);

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
}
