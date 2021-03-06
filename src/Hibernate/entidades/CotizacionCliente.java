package Hibernate.entidades;
// Generated 14/12/2016 10:27:20 AM by Hibernate Tools 3.6.0



/**
 * CotizacionCliente generated by hbm2java
 */
public class CotizacionCliente  implements java.io.Serializable {


     private Integer idCotizacionCliente;
     private Orden orden;
     private String descripcion;
     private String codigo;
     private String op;
     private Double cantidad;
     private String medida;
     private Double costo;
     private Integer autorizado;

    public CotizacionCliente() {
    }

	
    public CotizacionCliente(Integer autorizado) {
        this.autorizado = autorizado;
    }
    public CotizacionCliente(Orden orden, String descripcion, String codigo, String op, Double cantidad, String medida, Double costo, Integer autorizado) {
       this.orden = orden;
       this.descripcion = descripcion;
       this.codigo = codigo;
       this.op = op;
       this.cantidad = cantidad;
       this.medida = medida;
       this.costo = costo;
       this.autorizado = autorizado;
    }
   
    public Integer getIdCotizacionCliente() {
        return this.idCotizacionCliente;
    }
    
    public void setIdCotizacionCliente(Integer idCotizacionCliente) {
        this.idCotizacionCliente = idCotizacionCliente;
    }
    public Orden getOrden() {
        return this.orden;
    }
    
    public void setOrden(Orden orden) {
        this.orden = orden;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getOp() {
        return this.op;
    }
    
    public void setOp(String op) {
        this.op = op;
    }
    public Double getCantidad() {
        return this.cantidad;
    }
    
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    public String getMedida() {
        return this.medida;
    }
    
    public void setMedida(String medida) {
        this.medida = medida;
    }
    public Double getCosto() {
        return this.costo;
    }
    
    public void setCosto(Double costo) {
        this.costo = costo;
    }
    public Integer getAutorizado() {
        return this.autorizado;
    }
    
    public void setAutorizado(Integer autorizado) {
        this.autorizado = autorizado;
    }




}


