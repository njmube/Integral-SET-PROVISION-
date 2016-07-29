package Hibernate.entidades;
// Generated 21/08/2015 05:39:10 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Ejemplar generated by hbm2java
 */
public class Ejemplar  implements java.io.Serializable {


     private String idParte;
     private Marca marca;
     private Tipo tipo;
     private String catalogo;
     private Integer modelo;
     private String comentario;
     private String imagen;
     private Integer inventario;
     private String medida;
     private Double existencias;
     private Double precio;
     private Double maximo;
     private Double minimo;
     private Set partidas = new HashSet(0);
     private Set partidasExternas= new HashSet(0);
     private Set movimientos= new HashSet(0);

    public Ejemplar() {
    }

	
    public Ejemplar(String idParte) {
        this.idParte = idParte;
    }
    public Ejemplar(String idParte, Marca marca, Tipo tipo, String catalogo, Integer modelo, String comentario, String imagen, Integer inventario, String medida, Double existencias, Double precio, Double maximo, Double minimo, Set partidas, Set partidasExternas, Set movimientos) {
       this.idParte = idParte;
       this.marca = marca;
       this.tipo = tipo;
       this.catalogo = catalogo;
       this.modelo = modelo;
       this.comentario = comentario;
       this.imagen = imagen;
       this.inventario=inventario;
       this.medida=medida;
       this.existencias=existencias;
       this.precio = precio;
       this.maximo = maximo;
       this.minimo = minimo;
       this.partidas = partidas;
       this.partidasExternas=partidasExternas;
       this.movimientos=movimientos;
    }
   
    public String getIdParte() {
        return this.idParte;
    }
    
    public void setIdParte(String idParte) {
        this.idParte = idParte;
    }
    public Marca getMarca() {
        return this.marca;
    }
    
    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    public Tipo getTipo() {
        return this.tipo;
    }
    
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    public String getCatalogo() {
        return this.catalogo;
    }
    
    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }
    public Double getExistencias() {
        return this.existencias;
    }
    
    public void setExistencias(Double existencias) {
        this.existencias = existencias;
    }
    public Double getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
    public Double getMaximo() {
        return this.maximo;
    }
    
    public void setMaximo(Double maximo) {
        this.maximo = maximo;
    }
    public Double getMinimo() {
        return this.minimo;
    }
    
    public void setMinimo(Double minimo) {
        this.minimo = minimo;
    }
    public String getMedida() {
        return this.medida;
    }
    
    public void setMedida(String medida) {
        this.medida = medida;
    }
    
    public Integer getModelo() {
        return this.modelo;
    }
    
    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }
    public Integer getInventario() {
        return this.inventario;
    }
    
    public void setInventario(Integer inventario) {
        this.inventario = inventario;
    }
    
    public String getComentario() {
        return this.comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public String getImagen() {
        return this.imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public Set getPartidas() {
        return this.partidas;
    }
    
    public void setPartidas(Set partidas) {
        this.partidas = partidas;
    }

    public Set getPartidasExternas() {
        return this.partidasExternas;
    }
    
    public void setPartidasExternas(Set partidasExternas) {
        this.partidasExternas = partidasExternas;
    }


    public Set getMovimientos() {
        return this.movimientos;
    }
    
    public void setMovimientos(Set movimientos) {
        this.movimientos = movimientos;
    }
}


