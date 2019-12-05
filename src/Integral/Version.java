/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Angel
 */
public class Version {
    String ruta, local, version;
    public boolean version(){
        
        try
        {
            //archivo de red
            ruta="";
            version="";
            local="";
            //obtenemos direccion de red
            FileReader f = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(f);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
            
            //obtenemos la version de red
            String nueva=ruta+"ver.txt";
            FileReader archivo= new FileReader(nueva);
            BufferedReader b1 = new BufferedReader(archivo);
            if((version=b1.readLine())==null){
                version="";
            }
            b.close();
            
            //obtenemos la version local
            FileReader f1 = new FileReader("ver.txt");
            BufferedReader b2 = new BufferedReader(f1);
            if((local = b2.readLine())==null)
                local="";
            b.close();
            
            //comparamos las versiones red y local
            if(version.compareTo(local)!=0)
                return true;
            else
                return false;
        }
        catch(IOException e)
        {
           // e.printStackTrace();
            return false;
        }
    }
}
