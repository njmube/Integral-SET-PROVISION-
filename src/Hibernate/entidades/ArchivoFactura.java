package Hibernate.entidades;
// Generated 9/05/2016 11:25:54 AM by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * ArchivoFactura generated by hbm2java
 */
public class ArchivoFactura  implements java.io.Serializable {


     private Integer idFactura;
     private Reclamo reclamo;
     private String nombre;
     private String folio;
     private String uuid;
     private String moneda;
     private Double cambio;
     private Double subTotal;
     private Double iva;
     private String estatus;
     private String respuesta;
     private Date fecha;
     private String cancelado;

    public ArchivoFactura() {
    }

    public ArchivoFactura(Reclamo reclamo, String nombre, String folio, String uuid, String moneda, Double cambio, Double subTotal, Double iva, String estatus, String respuesta, Date fecha, String cancelado) {
       this.reclamo = reclamo;
       this.nombre = nombre;
       this.folio = folio;
       this.uuid = uuid;
       this.moneda = moneda;
       this.cambio = cambio;
       this.subTotal = subTotal;
       this.iva = iva;
       this.estatus = estatus;
       this.respuesta = respuesta;
       this.fecha = fecha;
       this.cancelado = cancelado;
    }
   
    public Integer getIdFactura() {
        return this.idFactura;
    }
    
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }
    public Reclamo getReclamo() {
        return this.reclamo;
    }
    
    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFolio() {
        return this.folio;
    }
    
    public void setFolio(String folio) {
        this.folio = folio;
    }
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public Double getCambio() {
        return this.cambio;
    }
    
    public void setCambio(Double cambio) {
        this.cambio = cambio;
    }
    public Double getSubTotal() {
        return this.subTotal;
    }
    
    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
    public Double getIva() {
        return this.iva;
    }
    
    public void setIva(Double iva) {
        this.iva = iva;
    }
    public String getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    public String getRespuesta() {
        return this.respuesta;
    }
    
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getCancelado() {
        return this.cancelado;
    }
    
    public void setCancelado(String cancelado) {
        this.cancelado = cancelado;
    }




}


