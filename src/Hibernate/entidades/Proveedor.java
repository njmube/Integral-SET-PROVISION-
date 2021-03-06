package Hibernate.entidades;
// Generated 21/08/2015 05:39:10 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Proveedor generated by hbm2java
 */
public class Proveedor  implements java.io.Serializable {


     private Integer idProveedor;
     private Cuentas cuentasByCtaProv;
     private Cuentas cuentasByCtaGasto;
     private String nombre;
     private String direccion;
     private String colonia;
     private String cp;
     private String poblacion;
     private String estado;
     private String rfc;
     private String tel1;
     private String tel2;
     private String fax;
     private String email;
     private String representante;
     private String cta;
     private Float credLimite;
     private int credPlazo;
     private float credDescuento;
     private int tiempoEntrega;
     private float impCompras;
     private String giro;
     private int tipo;
     private boolean espHojalateria;
     private boolean espMecanica;
     private boolean espSuspension;
     private boolean espElectricidad;
     private String notas;
     private String usuario;
     private String clave;
     private UsoCfdi usoCfdi;
     private Set cotizacions = new HashSet(0);
     private Set pedidosForIdProveedor = new HashSet(0);
     private Set pedidosForIdEmpresa = new HashSet(0);
     private Set partidas = new HashSet(0);

    public Proveedor() {
    }

	
    public Proveedor(String nombre, String rfc, int credPlazo, float credDescuento, int tiempoEntrega, float impCompras, int tipo, boolean espHojalateria, boolean espMecanica, boolean espSuspension, boolean espElectricidad) {
        this.nombre = nombre;
        this.rfc = rfc;
        this.credPlazo = credPlazo;
        this.credDescuento = credDescuento;
        this.tiempoEntrega = tiempoEntrega;
        this.impCompras = impCompras;
        this.tipo = tipo;
        this.espHojalateria = espHojalateria;
        this.espMecanica = espMecanica;
        this.espSuspension = espSuspension;
        this.espElectricidad = espElectricidad;
    }
    public Proveedor(Cuentas cuentasByCtaProv, Cuentas cuentasByCtaGasto, String nombre, String direccion, String colonia, String cp, String poblacion, String estado, String rfc, String tel1, String tel2, String fax, String email, String representante, String cta, Float credLimite, int credPlazo, float credDescuento, int tiempoEntrega, float impCompras, String giro, int tipo, boolean espHojalateria, boolean espMecanica, boolean espSuspension, boolean espElectricidad, String notas, String usuario, String clave, UsoCfdi usoCfdi, Set cotizacions, Set pedidosForIdProveedor, Set pedidosForIdEmpresa, Set partidas) {
       this.nombre = nombre;
       this.cuentasByCtaProv = cuentasByCtaProv;
       this.cuentasByCtaGasto = cuentasByCtaGasto;
       this.direccion = direccion;
       this.colonia = colonia;
       this.cp = cp;
       this.poblacion = poblacion;
       this.estado = estado;
       this.rfc = rfc;
       this.tel1 = tel1;
       this.tel2 = tel2;
       this.fax = fax;
       this.email = email;
       this.representante = representante;
       this.cta = cta;
       this.credLimite = credLimite;
       this.credPlazo = credPlazo;
       this.credDescuento = credDescuento;
       this.tiempoEntrega = tiempoEntrega;
       this.impCompras = impCompras;
       this.giro = giro;
       this.tipo = tipo;
       this.espHojalateria = espHojalateria;
       this.espMecanica = espMecanica;
       this.espSuspension = espSuspension;
       this.espElectricidad = espElectricidad;
       this.notas = notas;
       this.usuario = usuario;
       this.clave = clave;
       this.usoCfdi = usoCfdi;
       this.cotizacions = cotizacions;
       this.pedidosForIdProveedor = pedidosForIdProveedor;
       this.pedidosForIdEmpresa = pedidosForIdEmpresa;
       this.partidas = partidas;
    }
   
    public Integer getIdProveedor() {
        return this.idProveedor;
    }
    
    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getColonia() {
        return this.colonia;
    }
    
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    public String getCp() {
        return this.cp;
    }
    
    public void setCp(String cp) {
        this.cp = cp;
    }
    public String getPoblacion() {
        return this.poblacion;
    }
    
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getRfc() {
        return this.rfc;
    }
    
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    public String getTel1() {
        return this.tel1;
    }
    
    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }
    public String getTel2() {
        return this.tel2;
    }
    
    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRepresentante() {
        return this.representante;
    }
    
    public void setRepresentante(String representante) {
        this.representante = representante;
    }
    public String getCta() {
        return this.cta;
    }
    
    public void setCta(String cta) {
        this.cta = cta;
    }
    public Float getCredLimite() {
        return this.credLimite;
    }
    
    public void setCredLimite(Float credLimite) {
        this.credLimite = credLimite;
    }
    public int getCredPlazo() {
        return this.credPlazo;
    }
    
    public void setCredPlazo(int credPlazo) {
        this.credPlazo = credPlazo;
    }
    public float getCredDescuento() {
        return this.credDescuento;
    }
    
    public void setCredDescuento(float credDescuento) {
        this.credDescuento = credDescuento;
    }
    public int getTiempoEntrega() {
        return this.tiempoEntrega;
    }
    
    public void setTiempoEntrega(int tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }
    public float getImpCompras() {
        return this.impCompras;
    }
    
    public void setImpCompras(float impCompras) {
        this.impCompras = impCompras;
    }
    public String getGiro() {
        return this.giro;
    }
    
    public void setGiro(String giro) {
        this.giro = giro;
    }
    public int getTipo() {
        return this.tipo;
    }
    
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public boolean isEspHojalateria() {
        return this.espHojalateria;
    }
    
    public void setEspHojalateria(boolean espHojalateria) {
        this.espHojalateria = espHojalateria;
    }
    public boolean isEspMecanica() {
        return this.espMecanica;
    }
    
    public void setEspMecanica(boolean espMecanica) {
        this.espMecanica = espMecanica;
    }
    public boolean isEspSuspension() {
        return this.espSuspension;
    }
    
    public void setEspSuspension(boolean espSuspension) {
        this.espSuspension = espSuspension;
    }
    public boolean isEspElectricidad() {
        return this.espElectricidad;
    }
    
    public void setEspElectricidad(boolean espElectricidad) {
        this.espElectricidad = espElectricidad;
    }
    public String getNotas() {
        return this.notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public String getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public Cuentas getCuentasByCtaProv() {
        return this.cuentasByCtaProv;
    }
    
    public void setCuentasByCtaProv(Cuentas cuentasByCtaProv) {
        this.cuentasByCtaProv = cuentasByCtaProv;
    }
    public Cuentas getCuentasByCtaGasto() {
        return this.cuentasByCtaGasto;
    }
    
    public void setCuentasByCtaGasto(Cuentas cuentasByCtaGasto) {
        this.cuentasByCtaGasto = cuentasByCtaGasto;
    }
    
    public UsoCfdi getUsoCfdi() {
        return this.usoCfdi;
    }
    
    public void setUsoCfdi(UsoCfdi usoCfdi) {
        this.usoCfdi = usoCfdi;
    }
    
    public Set getCotizacions() {
        return this.cotizacions;
    }
    
    public void setCotizacions(Set cotizacions) {
        this.cotizacions = cotizacions;
    }
    public Set getPedidosForIdProveedor() {
        return this.pedidosForIdProveedor;
    }
    
    public void setPedidosForIdProveedor(Set pedidosForIdProveedor) {
        this.pedidosForIdProveedor = pedidosForIdProveedor;
    }
    public Set getPedidosForIdEmpresa() {
        return this.pedidosForIdEmpresa;
    }
    
    public void setPedidosForIdEmpresa(Set pedidosForIdEmpresa) {
        this.pedidosForIdEmpresa = pedidosForIdEmpresa;
    }
    public Set getPartidas() {
        return this.partidas;
    }
    
    public void setPartidas(Set partidas) {
        this.partidas = partidas;
    }

    

}


