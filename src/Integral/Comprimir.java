/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 *
 * @author salvador
 */


public class Comprimir {

public enum Extension {
ZIP (".zip"),
GZ (".gz"),
RAR (".rar");


private final String value;


Extension(String value) {
this.value = value;
}
public String getValue() {return value;}
}


    // Crea un zip desde un archivo.
    public static void crearZip(String origen, String destino,Extension extension) throws IOException 
    {
        FileInputStream in = new FileInputStream(origen);
        GZIPOutputStream out =
        new GZIPOutputStream(new FileOutputStream(destino+extension.getValue()));
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1)
            out.write(buffer, 0, bytesRead);
        in.close();
        out.close();
    }

    // Crea un zip desde un directorio.
    public static void zipDirectorio(String dir, String destino,Extension extension) throws IOException, IllegalArgumentException 
    {
        // Revisa que el directorio sea direcorio y lee sus archivos.
        File d = new File(dir);
        if (!d.isDirectory())
        throw new IllegalArgumentException(dir + " no es un directorio." );
        String[] entries = d.list();
        byte[] buffer = new byte[4096]; // Crea un buffer para copiar
        int bytesRead;
        ZipOutputStream out = new ZipOutputStream(new
        FileOutputStream(destino+extension.getValue()));
        for (int i = 0; i < entries.length; i++) 
        {
            File f = new File(d, entries[i]);
            if (f.isDirectory())
            continue; //Ignora el directorio
            FileInputStream in = new FileInputStream(f);

            ZipEntry entry = new ZipEntry(f.getPath()); //Crea una entrada zip para cada archivo,
            out.putNextEntry(entry);

            while ((bytesRead = in.read(buffer)) != -1)
            out.write(buffer, 0, bytesRead);
            in.close();
        }
        out.close();
    }
    
    public static void zipArchivos(List lista, String destino, Extension extension) throws IOException, IllegalArgumentException 
    {
        byte[] buffer = new byte[4096]; // Crea un buffer para copiar
        int bytesRead;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destino+extension.getValue()));
        for (int i = 0; i < lista.size(); i++) 
        {
            File f = new File(lista.get(i).toString());
            FileInputStream in = new FileInputStream(f);
            ZipEntry entry = new ZipEntry(f.getPath()); //Crea una entrada zip para cada archivo,
            out.putNextEntry(entry);
            while ((bytesRead = in.read(buffer)) != -1)
            out.write(buffer, 0, bytesRead);
            in.close();
        }
        out.close();
    }
}
