/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;
import org.apache.commons.net.ftp.*;
import org.apache.poi.util.IOUtils;
/**
 *
 * @author I.S.C.Salvador
 */
public class Ftp {
     private static String actual="/";
     private static FTPClient cliente;
     private static String servidor="";
     public static String raiz="";
     
     public Ftp()
     {
         cliente = new FTPClient();
     }
     /**
     * Conecta a un servidor mediante usuario y contraseña.
     * @param server Servidor
     * @param user Usuario.
     * @param pwd Contraseña.
     * @return True, si la conexión se estableció y False, es caso contrario.
     */
    public static boolean conectar(String server, String user, String pwd, int port)
    {     
        try {
            cliente.connect(server, port);
            if(cliente.login(user, pwd)){
                cliente.enterLocalPassiveMode();
                cliente.setFileType(FTP.BINARY_FILE_TYPE);

                int respuesta = cliente.getReplyCode();
                if (FTPReply.isPositiveCompletion(respuesta) == true)
                {
                    servidor=server;
                    actual =  cliente.printWorkingDirectory();
                    raiz=actual;
                    return true;
                }
                else
                {
                    servidor="";
                    return false;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Host del servidor incorrecto: "+server);
            e.printStackTrace();
            return false;
        }           
    }
    
    /**
     * Cierra sesión y se desconecta del servidor.
     * @return True, si la desconexión fue correcta.<br>
     * False, en caso contrario.
     */
    public static boolean desconectar() {
        try {
            // Cerrar sesion y desconectar.
            cliente.logout();
            cliente.disconnect();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Obtener la ubicación actual.
     * @return Un String con la ubicación actual.
     */
    public static String DirectorioActual() {
        return actual;
    }
    
    /**
     * Cambia directorio.
     * @param carpeta Dirección completa de la carpeta
     * en el servidor.
     * @return True, si ha cambiado de directorio.<br>
     * False, en caso contrario.
     */ 
    public static boolean cambiarDirectorio(String carpeta) {        
        String direccion="";
        try {
            if(servidor.compareTo("tbstultitlan.ddns.net")==0)
            {
                cliente.changeWorkingDirectory(raiz);
                actual =  cliente.printWorkingDirectory();
                direccion=actual+carpeta;
            }
            else
                direccion=carpeta;
            System.out.println(direccion);
            if(cliente.changeWorkingDirectory(direccion)==true)
            {
                actual =  cliente.printWorkingDirectory();
                System.out.println(actual);
                return true;
            }
            else
            {
                System.out.println(actual);
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cambiar de directorio");
            e.printStackTrace();
            return false;
        }       
    }
    
    /**
     * Descarga un fichero del servidor y lo almacena en una ruta específica.
     * @param archivoFtp Nombre del archivo a descargar.
     * @param archivoLocal Dirección donde se quiere almacenar el archivo.
     * @return True, si la descarga y almacenamiento ha sido correcto.
     * False, en caso contrario.
     */
    public static boolean descargaArchivo(String archivo, String carpeta){
        OutputStream os;
        boolean correcto = false;
        try {
            System.out.println(carpeta + archivo);
            os = new BufferedOutputStream(new FileOutputStream(carpeta + archivo));
            correcto = cliente.retrieveFile(archivo, os);
            os.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al descargar el archivo");
            e.printStackTrace();
        }
        return correcto;      
    }
    
    
    /**
     * Descarga un fichero del servidor y lo almacena en temporales
     * @param archivoFtp Nombre del archivo a descargar.
     * @return ruta del archivo local
     */
    public static String descargaTemporal(String archivo){
        String correcto="";
        try{
            System.out.println(actual+"/"+archivo);
            String aux = archivo.substring(archivo.lastIndexOf(".") + 1);
            InputStream in=cliente.retrieveFileStream(actual+"/"+archivo);
            File temp = File.createTempFile("tmp", "."+aux);
            OutputStream out = new FileOutputStream(temp.getAbsolutePath());
            IOUtils.copy(in,out);
            in.close();
            out.close();
            temp.deleteOnExit();
            correcto=temp.getAbsolutePath();
        }catch(Exception e){
            e.printStackTrace();
        }
        return correcto;      
    }
    
    
    /**
     * Descarga un fichero del servidor y lo almacena en una ruta específica.
     * @param archivoFtp Nombre del archivo a descargar.
     * @param archivoLocal Dirección donde se quiere almacenar el archivo.
     * @return True, si la descarga y almacenamiento ha sido correcto.
     * False, en caso contrario.
     */
    public static boolean AbrirArchivo(String archivo){
        boolean correcto=false;
        try{
            String aux = archivo.substring(archivo.lastIndexOf(".") + 1);
            System.out.println(actual+"/"+archivo);
            InputStream in=cliente.retrieveFileStream(actual+"/"+archivo);
            File temp = File.createTempFile("tmp", "."+aux);
            OutputStream out = new FileOutputStream(temp.getAbsolutePath());
            IOUtils.copy(in,out);
            in.close();
            out.close();
            Desktop.getDesktop().open(temp);
            temp.deleteOnExit();
            correcto=true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo");
            e.printStackTrace();
        }
        return correcto;      
    }
    /**
     * Borra un archivo en la ubicación actual del servidor.
     * @param archivo Nombre archivo a borrar.
     * @return True, si se ha borrado. 
     * False, en caso contrario.
     */
    public static boolean borrarArchivo(String archivo){
        boolean borrado = false;
        try{
            borrado = cliente.deleteFile(archivo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al borrar el archivo");
        }
        return borrado;
    }
    
    /**
     * Sube un Archivo en la ubicación actual.
     * @param carpeta Dirección de la carpeta que contiene el archivo Ejm: /Integral/ordenes/
     * @param archivo Nombre del archivo y extensión. Ejm: 170300.jpg
     * @return True, si ha subido de forma satisfactoria el fichero, False, en caso contrario.
     */
    public static boolean subirArchivo(String carpeta, String archivo){
        InputStream is;
        boolean fichSubido = false;

        try {           
            is = new BufferedInputStream(new FileInputStream(carpeta));
            fichSubido = cliente.storeFile(archivo, is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fichSubido;
    }
    
    public static FTPFile[] listarArchivos()
    {
        try{
            return cliente.listFiles();
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean crearDirectorio(String ruta)
    {
        try{
            //if(servidor.compareTo("set-tultitlan.ddns.net")==0)
                ruta=ruta.substring(1,ruta.length());
            System.out.println("crear dir:"+ruta);
            return cliente.makeDirectory(ruta);
        }catch(Exception e)
        {
            System.out.println("Error al crear dir:"+ruta);
            e.printStackTrace();
            return false;
        }
    }
}
