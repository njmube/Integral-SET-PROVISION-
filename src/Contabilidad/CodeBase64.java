package Contabilidad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.ssl.Base64;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salvador
 */
public class CodeBase64 {
    public boolean DecodeBase64(String cadena, String archivo)
    {
        try{
        Base64.Decoder decoder= Base64.getDecoder();
        byte[] fileArray=decoder.decode(cadena);
        File f = new File("salida.txt");
        FileOutputStream fos = new FileOutputStream(archivo);
        fos.write(fileArray);
        fos.close();
        if(archivo.contains(".xml")==true)
            limpia(archivo);
        return true;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public String EncodeArchivo(File file)
    {
        byte[] fileArray = new byte[(int) file.length()];
        InputStream inputStream;
        String encodedFile = "";
        try
        {
            inputStream = new FileInputStream(file);
            inputStream.read(fileArray);
            Base64.Encoder encoder = Base64.getEncoder();
            encodedFile=encoder.encodeToString(fileArray);
            /*File f = new File("salida.txt");
            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);  
            wr.write(encodedFile);
            wr.close();
            bw.close();*/
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return encodedFile;
    }
    
    public void limpia(String ruta)
    {
        try
        {
            org.jdom2.Document doc = new SAXBuilder().build(ruta);
            Element rootNode = doc.getRootElement();
            List list = rootNode.getContent();
            for ( int i = 0; i < list.size(); i++ )
            {
                Content elementos = (Content) list.get(i);
                if(elementos.getCType()==Content.CType.Element)
                {
                    Element aux=(Element)elementos;
                    if(aux.getName().compareToIgnoreCase("Addenda")==0)
                    {
                        List list2 = aux.getContent();
                        for ( int j = 0; j < list2.size(); j++ )
                        {
                            Content elementos2 = (Content) list2.get(j);
                            if(elementos2.getCType()==Content.CType.Element)
                            {
                                Element aux2=(Element)elementos2;
                                if(aux2.getName().compareToIgnoreCase("FactDocMX")==0)
                                {
                                    list2.remove(aux2);
                                }
                                if(aux2.getName().compareToIgnoreCase("ECFD")==0)
                                {
                                    Namespace NP=Namespace.getNamespace("", "");
                                    aux2.setNamespace(NP);
                                    List list3 = aux2.getContent();
                                    for ( int k = 0; k < list3.size(); k++ )
                                    {
                                        Content elementos3 = (Content) list3.get(k);
                                        if(elementos3.getCType()==Content.CType.Element)
                                        {
                                            Element aux3=(Element)elementos3;
                                            aux3.setNamespace(NP);
                                            List list4 = aux3.getContent();
                                            for ( int l = 0; l < list4.size(); l++ )
                                            {
                                                Content elementos4 = (Content) list4.get(l);
                                                if(elementos4.getCType()==Content.CType.Element)
                                                {
                                                    Element aux4=(Element)elementos4;
                                                    aux4.setNamespace(NP);
                                                    List list5 = aux4.getContent();
                                                    for ( int m = 0; m < list5.size(); m++ )
                                                    {
                                                        Content elementos5 = (Content) list5.get(m);
                                                        if(elementos5.getCType()==Content.CType.Element)
                                                        {
                                                            Element aux5=(Element)elementos5;
                                                            aux5.setNamespace(NP);
                                                            List list6 = aux5.getContent();
                                                            for ( int n = 0; n < list6.size(); n++ )
                                                            {
                                                                Content elementos6 = (Content) list6.get(n);
                                                                if(elementos6.getCType()==Content.CType.Element)
                                                                {
                                                                    Element aux6=(Element)elementos6;
                                                                    aux6.setNamespace(NP);
                                                                    List list7 = aux6.getContent();
                                                                    for ( int p = 0; p < list7.size(); p++ )
                                                                    {
                                                                        Content elementos7 = (Content) list7.get(p);
                                                                        if(elementos7.getCType()==Content.CType.Element)
                                                                        {
                                                                            Element aux7=(Element)elementos7;
                                                                            aux7.setNamespace(NP);
                                                                             List list8 = aux7.getContent();
                                                                            for ( int q = 0; q < list8.size(); q++ )
                                                                            {
                                                                                Content elementos8 = (Content) list8.get(q);
                                                                                if(elementos8.getCType()==Content.CType.Element)
                                                                                {
                                                                                    Element aux8=(Element)elementos8;
                                                                                    aux8.setNamespace(NP);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    List atributos=aux2.getAttributes();
                                    for(int a=0; a <atributos.size(); a++)
                                    {
                                        Attribute at= (Attribute) atributos.get(a);
                                        if(at.getName().compareToIgnoreCase("schemaLocation")==0)
                                            aux2.removeAttribute(at); 
                                    }
                                }
                            }
                        }
                    }
                }
            }
            XMLOutputter outputter = new XMLOutputter( Format.getPrettyFormat() );
            try {
               outputter.output(doc, new FileOutputStream (ruta));
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
