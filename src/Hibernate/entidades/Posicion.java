/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Hibernate.entidades;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author I.S.C.Salvador
 */
public class Posicion {
    private int idPosicion;
     private String descripcion;
     private Set items = new HashSet(0);

    public Posicion() {
    }

	
    public Posicion(int idPosicion) {
        this.idPosicion = idPosicion;
    }
    public Posicion(int idPosicion, String descripcion, Set items) {
       this.idPosicion = idPosicion;
       this.descripcion = descripcion;
       this.items = items;
    }
   
    public int getIdPosicion() {
        return this.idPosicion;
    }
    
    public void setIdPosicion(int idPosicion) {
        this.idPosicion = idPosicion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
  
    public Set getItems() {
        return this.items;
    }
    
    public void setItems(Set items) {
        this.items = items;
    }

}
