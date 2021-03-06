package Hibernate.entidades;
// Generated 21/08/2015 05:39:10 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Especialidad generated by hbm2java
 */
public class Especialidad  implements java.io.Serializable {


     private Integer idGrupoMecanico;
     private String descripcion;
     private Boolean plantilla;
     private Integer orden;
     private Set catalogos = new HashSet(0);

    public Especialidad() {
    }

    public Especialidad(String descripcion, Boolean plantilla, Set catalogos, Integer orden) {
       this.descripcion = descripcion;
       this.plantilla = plantilla;
       this.orden = orden;
       this.catalogos = catalogos;
    }
   
    public Integer getIdGrupoMecanico() {
        return this.idGrupoMecanico;
    }
    
    public void setIdGrupoMecanico(Integer idGrupoMecanico) {
        this.idGrupoMecanico = idGrupoMecanico;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Boolean getPlantilla() {
        return this.plantilla;
    }
    
    public void setPlantilla(Boolean plantilla) {
        this.plantilla = plantilla;
    }

    public Integer getOrden() {
        return this.orden;
    }
    
    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    
    public Set getCatalogos() {
        return this.catalogos;
    }
    
    public void setCatalogos(Set catalogos) {
        this.catalogos = catalogos;
    }




}


