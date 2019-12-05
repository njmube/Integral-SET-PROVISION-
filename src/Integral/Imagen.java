/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author I.S.C Salvador
 */
public class Imagen extends javax.swing.JPanel {
        private String ruta;
        private int w, h, ml, mt, x1, y1;
        public ImageIcon dibujo=null;
        public BufferedImage img = null;
        public Imagen(String img, int largo, int ancho, int margen1, int margen2, int xpanel, int ypanel) {
            ruta=img;
            x1=xpanel;
            y1=ypanel;
            this.setSize(x1, y1); 
            w=largo;
            h=ancho;
            ml=margen1;
            mt=margen2;
        }
        
        //Se crea un método cuyo parámetro debe ser un objeto Graphics
        public void paint(Graphics grafico) 
        {
            try 
            {
                File arch;
                arch=new File(ruta);
                if(arch.exists())
                {
                    img = ImageIO.read(arch);
                    dibujo = new ImageIcon(img);
                    grafico.drawImage(dibujo.getImage(), ml, mt, w, h, null); // miniatura (97x99)
                    setOpaque(false);
                    super.paintComponent(grafico);
                    dibujo=null;
                    img=null;
                    grafico=null;
                }
                else
                {
                    img = ImageIO.read(new File("imagenes/foto.png"));
                    dibujo = new ImageIcon(img);
                    grafico.drawImage(dibujo.getImage(), ml, mt, w, h, null); // miniatura (97x99)
                    setOpaque(false);
                    super.paintComponent(grafico);
                    dibujo=null;
                    img=null;
                    grafico=null;
                }
            } catch (IOException ex) 
            {
                System.out.println("No se pudo leer la imagen:"+ruta);
            }
        }
    }
