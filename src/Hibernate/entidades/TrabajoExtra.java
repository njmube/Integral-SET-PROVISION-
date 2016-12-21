package Hibernate.entidades;
// Generated 21/12/2016 12:54:15 PM by Hibernate Tools 3.6.0


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TrabajoExtra generated by hbm2java
 */
public class TrabajoExtra  implements java.io.Serializable {


     private Integer idAdicional;
     private Orden orden;
     private Empleado empleado;
     private Date fechaDestajo;
     private Double horas;
     private Double importe;
     private String notas;
     private Byte autorizado;
     private String idAutorizado;
     private Set almacens = new HashSet(0);
     private Set pagoAdicionals = new HashSet(0);

    public TrabajoExtra() {
    }

    public TrabajoExtra(Orden orden, Empleado empleado, Date fechaDestajo, Double horas, Double importe, String notas, Byte autorizado, String idAutorizado, Set almacens, Set pagoAdicionals) {
       this.orden = orden;
       this.empleado = empleado;
       this.fechaDestajo = fechaDestajo;
       this.horas = horas;
       this.importe = importe;
       this.notas = notas;
       this.autorizado = autorizado;
       this.idAutorizado = idAutorizado;
       this.almacens = almacens;
       this.pagoAdicionals = pagoAdicionals;
    }
   
    public Integer getIdAdicional() {
        return this.idAdicional;
    }
    
    public void setIdAdicional(Integer idAdicional) {
        this.idAdicional = idAdicional;
    }
    public Orden getOrden() {
        return this.orden;
    }
    
    public void setOrden(Orden orden) {
        this.orden = orden;
    }
    public Empleado getEmpleado() {
        return this.empleado;
    }
    
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    public Date getFechaDestajo() {
        return this.fechaDestajo;
    }
    
    public void setFechaDestajo(Date fechaDestajo) {
        this.fechaDestajo = fechaDestajo;
    }
    public Double getHoras() {
        return this.horas;
    }
    
    public void setHoras(Double horas) {
        this.horas = horas;
    }
    public Double getImporte() {
        return this.importe;
    }
    
    public void setImporte(Double importe) {
        this.importe = importe;
    }
    public String getNotas() {
        return this.notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    public Byte getAutorizado() {
        return this.autorizado;
    }
    
    public void setAutorizado(Byte autorizado) {
        this.autorizado = autorizado;
    }
    public String getIdAutorizado() {
        return this.idAutorizado;
    }
    
    public void setIdAutorizado(String idAutorizado) {
        this.idAutorizado = idAutorizado;
    }
    public Set getAlmacens() {
        return this.almacens;
    }
    
    public void setAlmacens(Set almacens) {
        this.almacens = almacens;
    }
    public Set getPagoAdicionals() {
        return this.pagoAdicionals;
    }
    
    public void setPagoAdicionals(Set pagoAdicionals) {
        this.pagoAdicionals = pagoAdicionals;
    }




}


