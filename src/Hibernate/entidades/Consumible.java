package Hibernate.entidades;
// Generated 13/02/2017 09:18:03 AM by Hibernate Tools 3.6.0



/**
 * Consumible generated by hbm2java
 */
public class Consumible  implements java.io.Serializable {


     private int idConsumible;
     private Ejemplar ejemplar;
     private TrabajoExtra trabajoExtra;
     private Orden orden;
     private String medida;
     private double cantidad;
     private double precio;
     private String especialidad;
     private String tipo;

    public Consumible() {
    }

	
    public Consumible(int idConsumible, double cantidad, double precio, String especialidad) {
        this.idConsumible = idConsumible;
        this.cantidad = cantidad;
        this.precio = precio;
        this.especialidad = especialidad;
    }
    public Consumible(int idConsumible, Ejemplar ejemplar, TrabajoExtra trabajoExtra, Orden orden, String medida, String especialidad, String tipo) {
       this.idConsumible = idConsumible;
       this.ejemplar = ejemplar;
       this.trabajoExtra = trabajoExtra;
       this.orden = orden;
       this.medida = medida;
       this.cantidad = cantidad;
       this.precio = precio;
       this.especialidad = especialidad;
       this.tipo = tipo;
    }
   
    public int getIdConsumible() {
        return this.idConsumible;
    }
    
    public void setIdConsumible(int idConsumible) {
        this.idConsumible = idConsumible;
    }
    public Ejemplar getEjemplar() {
        return this.ejemplar;
    }
    
    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }
    public TrabajoExtra getTrabajoExtra() {
        return this.trabajoExtra;
    }
    
    public void setTrabajoExtra(TrabajoExtra trabajoExtra) {
        this.trabajoExtra = trabajoExtra;
    }
    
    public Orden getOrden() {
        return this.orden;
    }
    
    public void setOrden(Orden orden) {
        this.orden = orden;
    }
    public String getMedida() {
        return this.medida;
    }
    
    public void setMedida(String medida) {
        this.medida = medida;
    }
    public double getCantidad() {
        return this.cantidad;
    }
    
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getEspecialidad() {
        return this.especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    

    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}


