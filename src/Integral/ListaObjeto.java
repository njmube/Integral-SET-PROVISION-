/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import javax.swing.ImageIcon;

/**
 *
 * @author I.S.C.Salvador
 */
public class ListaObjeto {
    private String texto;
    private int id;
    private ImageIcon icono;
  
    public ListaObjeto(int id, String texto, ImageIcon icono) {
        this.id = id;
        this.texto = texto;
        this.icono = icono;
    }
  
    public int getId(){
        return id;
    }
    
    public String getTexto() {
        return texto;
    }
  
    public ImageIcon getIcono() {
        return icono;
    }
  
    public String toString() {
        return texto;
    }
}
