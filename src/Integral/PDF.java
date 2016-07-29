/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author I.S.C Salvador
 */
public class PDF {
    private Document document;
    public PdfWriter writer;
    public PdfContentByte contenido;
    public String ruta;
    public HeaderFooter even;
    public static String autoriza1="", autoriza2="";
    public PDF()
    {
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
    }
    public void estatusAutoriza(String aut, String aut1)
    {
        autoriza1=aut;
        autoriza2=aut1;
    }
    /**
     * Abre un documento nuevo para escribir un pdf
     * @param tamanio Tamaño de la hoja.
     * @param titulo Titulo del documento.
     * @param nombre nombre del documento con el que se guardara.
     * @return "true" si fue correcto "false" si hay error.
     */
    public boolean Abrir(Rectangle tamanio, String titulo, String nombre)
    {
        try
        {
            String [] aux=nombre.split("/");
            String valor="";
            for(int x=0; x<aux.length-1; x++)
                valor+=aux[x]+"/";
            File folder = new File(ruta+valor);
            folder.mkdirs();
            document = new Document();
            document.setPageSize(tamanio);
            //document.setMargins(10f, 10f, 80f, 80f);
            document.addTitle(titulo);
            writer = PdfWriter.getInstance(document, new FileOutputStream(ruta+nombre));
            if(titulo.compareToIgnoreCase("cabecera")==0)
            {
                HeaderFooter event = new HeaderFooter();
                writer.setPageEvent(event);
            }
            document.open();
            contenido = writer.getDirectContent();

            return true;
        }catch(DocumentException | FileNotFoundException e)
        {
            return false;
        }
    }
    
    public boolean Abrir2(Rectangle tamanio, String titulo, String nombre)
    {
        try
        {
            document = new Document();
            document.setPageSize(tamanio);
            //document.setMargins(10f, 10f, 80f, 80f);
            document.addTitle(titulo);
            writer = PdfWriter.getInstance(document, new FileOutputStream(nombre));
            if(titulo.compareToIgnoreCase("cabecera")==0)
            {
                HeaderFooter event = new HeaderFooter();
                writer.setPageEvent(event);
            }
            document.open();
            contenido = writer.getDirectContent();
            
            return true;
        }catch(DocumentException | FileNotFoundException e)
        {
            return false;
        }
    }
    /**
     * Cierra un documento abierto de tipo pdf
     * @return "true" si se pudo cerrar "false" so hay error.
     */
    public boolean cerrar()
    {
        try
        {
            document.close();
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
    }
    
    /**
     * Inicia documento para escribir en el.
     * @return "true" si no hay error de lo contrario retorna "false"
     */
    public boolean inicioTexto()
    {
        try
        {
            contenido.beginText();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    /**
     * Finaliza documeto para escribir texto en el.
     * @return "true" si no hay error de lo contrario retorna "false"
     */
    public boolean finTexto()
    {
        try
        {
            contenido.endText();
            contenido.stroke();
            contenido.closePathFillStroke();
            return true;
        }catch(Exception e)
        {
            return false;
        }
    }
    /**
     * Permite agregar texto a un documento
     * @param texto Cadena a escribir
     * @param fuente tipo de fuente
     * @param color color del texto
     * @param tamanio tamaño de la fuente
     * @param x posision en x para escribir el texto
     * @param y posision en y para escribir el texto
     * @return 
     */
    public boolean texto(String texto, BaseFont fuente, BaseColor color, int tamanio, int x, int y)
    {
        try
        {
            contenido.setFontAndSize(fuente, tamanio);
            contenido.setColorFill(color);
            contenido.setTextMatrix(x, y);
            contenido.showText(texto);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public boolean parrafo(String texto, Font fuente, int alineacion)
    {
        try
        {
            Paragraph parrafo=new Paragraph(texto, fuente);
            parrafo.setAlignment(alineacion);
            document.add(parrafo);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public boolean texto(String texto, BaseFont fuente, BaseColor color, int tamanio, int x, int y, int rotacion)
    {
        try
        {
            contenido.setFontAndSize(fuente, tamanio);
            contenido.setColorFill(color);
            contenido.setTextMatrix(x, y);
            //contenido.showText(texto);
            contenido.showTextAligned(0, texto, x, y, rotacion);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    /**
     * Manda un documento a la impresora
     * @param archivo nombre del archivo
     * @return "true" si no hay error de lo contrario retorna "false"
     */
    public boolean imprimir(String archivo) 
    {
        try
        {
            Desktop.getDesktop().print(new File(ruta+archivo));
            return true;
        }
        catch(IOException e)
        {
            System.out.println(e);
            return false;
        }
    }
    /**
     * Abre un archivo con el programa predeterminado para visualizar
     * @param archivo nombre del archivo
     * @return "true" si no hay error de lo contrario retorna "false"
     */
    public boolean visualizar(String archivo) 
    {
        try
        {
            File arch=new File(ruta+archivo);
            Desktop.getDesktop().open(arch.getAbsoluteFile());
            return true;
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(new JFrame(), "no se pudo encontrar el archivo"+e);
            return false;
        }
    }
    
    public boolean visualizar2(String archivo) 
    {
        try
        {
            File arch=new File(archivo);
            Desktop.getDesktop().open(arch.getAbsoluteFile());
            return true;
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(new JFrame(), "no se pudo encontrar el archivo"+e);
            return false;
        }
    }
    
    /**
     * Crea una objeto de tipo tabla
     * @param columnas numero de columnas
     * @param tamanio Tamaño de las columnas
     * @param porcentaje proporcion de la tabla deacuerdo a la hoja.
     * @param alineacion alineacion de la tabla respecto a la hoja
     * @return 
     */
    public PdfPTable crearTabla(int columnas, float tamanio[], int porcentaje, int alineacion)
    {
        try
        {
            PdfPTable table = new PdfPTable(columnas);
            table.setWidths(tamanio);
            table.setWidthPercentage(porcentaje);
            table.setHorizontalAlignment(alineacion);
            return table;
        }catch(DocumentException e)
        {
            System.out.println(e);
            return null;
        }
    }
    /**
     * crea una celda con una imagen adentro para una tabla
     * @param img imagen a incluir en la celda
     * @param color fondo de la celda
     * @param alineacion alineacion del contenido de la celda
     * @param expandirCol expandir la celda x columnas ej:3 junta la celda y 2 mas a la derecha
     * @param expandirRow Expandir la celda x renglones ej: 3 junta la celda y 2 mas hacia abajo
     * @param borde tipo de borde de la celda
     * @return 
     */
    public PdfPCell celda(Image img, BaseColor color, int alineacion, int expandirCol, int expandirRow, int borde)
    {
        PdfPCell c1 = new PdfPCell(img);
        c1.setColspan(expandirCol);
        c1.setRowspan(expandirRow);
        c1.setBackgroundColor(color);
        c1.setHorizontalAlignment(alineacion);
        c1.setBorder(borde);
        return c1;
    }
    /**
     * crea una celda con texto adentro para una tabla
     * @param texto Texto que se va a incluir en la celda
     * @param font Fuente para el texto
     * @param color color del texto
     * @param alineacion alineacion del texto respecto a la celda
     * @param expandirCol expandir la celda x columnas ej:3 junta la celda y 2 mas a la derecha
     * @param expandirRow Expandir la celda x renglones ej: 3 junta la celda y 2 mas hacia abajo
     * @param borde tipo de borde de la celda
     * @return 
     */
    public PdfPCell celda(String texto, Font font, BaseColor color, int alineacion, int expandirCol, int expandirRow,int borde)
    {
        try
        {
            PdfPCell c1 = new PdfPCell(new Paragraph(new Chunk(texto, font)));
            c1.setColspan(expandirCol);
            c1.setRowspan(expandirRow);
            c1.setBackgroundColor(color);
            c1.setHorizontalAlignment(alineacion);
            c1.setBorder(borde);
            
        return c1;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Agrega una tabla al documento.
     * @param table objeto de tipo PdfPTable
     * @return "true" si no hay error de lo contrario retorna "false"
     */
    public boolean agregarTabla(PdfPTable table) 
    {
        try
        {
            document.add(table);
            return true;
        }
        catch(DocumentException e)
        {
            System.out.println(e);
            return false;
        }
    }
      
    /**
     * Crea una imagen
     * @param img Ruta de la imagen
     * @return  Retorna un objeto de tipo Image
     */
    public Image Imagen(String img)
    {
        try
        {
            Image image = Image.getInstance(img);
            return image;
        }catch(Exception e)
        {
            return null;
        }
    }
    /**
     * Crea un objeto imagen de tipo Chunk
     * @param archivo Ruta de la imagen
     * @param x Posision en x para la imagen dentro del archivo
     * @param y Posision en y para la imagen dentro del archivo
     * @param escala escala de la imagen en porcentaje
     * @return Retorna una objeto de tipo Chunk
     */
    public Chunk crearImagen(String archivo, int x, int y, int escala) 
    {
        try
        {
            RandomAccessFile rf1 = new RandomAccessFile(ruta+archivo, "r");
            int size1 = (int)rf1.length();
            byte imagedata[] = new byte[size1];
            rf1.readFully(imagedata);
            rf1.close();
            Image img11 = Image.getInstance(imagedata);
            img11.scalePercent(escala);
            Chunk logo = new Chunk(img11, x, y);
            return logo;
        }catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    /**
     * Crea un objeto imagen de tipo Chunk
     * @param archivo Ruta de la imagen
     * @param x Posision en x para la imagen dentro del archivo
     * @param y Posision en y para la imagen dentro del archivo
     * @param w_escala escala de la imagen en relacion horizontal
     * @param h_escala escala de la imagen en relacion a vertical
     * @param grados grados para girar la imagen
     * @return Retorna una objeto de tipo Chunk
     */
    public Chunk crearImagen(String archivo, int x, int y, int w_escala, int h_escala, float grados) 
    {
        try
        {
            RandomAccessFile rf1 = new RandomAccessFile(ruta+archivo, "r");
            int size1 = (int)rf1.length();
            byte imagedata[] = new byte[size1];
            rf1.readFully(imagedata);
            rf1.close();
            Image img11 = Image.getInstance(imagedata);
            img11.scaleAbsolute(w_escala, h_escala);
            img11.setRotationDegrees(grados);
            Chunk logo = new Chunk(img11, x, y);
            return logo;
        }catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    /**
     * Agrega una imagen de tipo Chunk al documento
     * @param logo objeto que contiene la imagen de tipo Chunk
     * @return "true" si no hay error de lo contrario retorna "false"
     */
    public boolean agregarImagen(Chunk logo) 
    {
        try
        {
            document.add(logo);
            return true;
        }
        catch(DocumentException e)
        {
            System.out.println(e);
            return false;
        }
    }
            
    /**
     * Agrega elementos a un documento
     * @param obj Objeto de tipo Element a agregar
     * @return "true" si se pudo agregar de lo contrario retorna "false"
     */
    public boolean agregaObjeto(Element obj)
    {
        try
        {
            document.add(obj);
            return true;
        }catch(DocumentException e)
        {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }
    
    
public static class HeaderFooter extends PdfPageEventHelper {

        public void onEndPage (PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            //Aquí definimos el encabezado de nuestro documento PDF
           //Únicamente le ponemos nuestro nombre
            /*ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase("Roberto León Encabezado"),
                    rect.getRight(), rect.getTop(), 0);*/
            ColumnText.showTextAligned(writer.getDirectContent(),PdfContentByte.ALIGN_LEFT, new Phrase("_____________________________"), 50, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),PdfContentByte.ALIGN_LEFT, new Phrase("Solicita"), 120, 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),PdfContentByte.ALIGN_LEFT, new Phrase(autoriza1), 370, 40, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),PdfContentByte.ALIGN_LEFT, new Phrase(autoriza2), 370, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),PdfContentByte.ALIGN_LEFT, new Phrase("_____________________________"), 370, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),PdfContentByte.ALIGN_LEFT, new Phrase("Autoriza"), 440, 20, 0);
            
        /*reporte.contenido.roundRectangle(50, 20, 180, 1, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Solicita", 120, 10, 0);
        reporte.contenido.roundRectangle(370, 20, 180, 1, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Autoriza", 440, 10, 0);*/
        }
    }
}
