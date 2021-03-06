package Hibernate.entidades;
// Generated 21/08/2015 05:39:10 PM by Hibernate Tools 3.6.0

import java.util.Date;




/**
 * Configuracion generated by hbm2java
 */
public class Configuracion  implements java.io.Serializable {


     private int idConfiguracion;
     private Usuario usuario;
     private String empresa;
     private String logo;
     private String sello;
     private String cinta;
     private String facturaLogo;
     private Integer iva;
     private String requestor;
     private String rfc;
     private String usuario_1;
     private String nombre;
     private String cp;
     private String direccion;
     private String no;
     private String colonia;
     private String municipio;
     private String estado;
     private String pais;
     private String contacto;
     private String mail;
     private String tel;
     private String sucursal;
     private String emailFinkok;
     private String claveFinkok;
     private String pac;
     private String cer;
     private String llave;
     private String clave;
     private String serie;
     private String serie1;
     private String serie2;
     private Double version;
     private Double tipoCambio;
     private Date fechaCambio;
     private String noProveedor;

    public Configuracion() {
    }

	
    public Configuracion(int idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public Configuracion(int idConfiguracion, Usuario usuario, String empresa, String logo, String sello, String cinta, String facturaLogo, Integer iva, String requestor, String rfc, String usuario_1, String nombre, String cp, String direccion, String no, String colonia, String municipio, String estado, String pais, String contacto, String mail, String sucursal, String tel, String emailFinkok, String claveFinkok, String pac, String cer, String llave, String clave, String serie, String serie1, String serie2, Double version, Double tipoCambio, Date fechaCambio,String noProveedor) {
       this.idConfiguracion = idConfiguracion;
       this.usuario = usuario;
       this.empresa = empresa;
       this.logo = logo;
       this.sello = sello;
       this.cinta = cinta;
       this.facturaLogo=facturaLogo;
       this.iva = iva;
       this.requestor = requestor;
       this.rfc = rfc;
       this.usuario_1 = usuario_1;
       this.nombre = nombre;
       this.cp = cp;
       this.direccion = direccion;
       this.no = no;
       this.colonia = colonia;
       this.municipio = municipio;
       this.estado = estado;
       this.pais = pais;
       this.contacto = contacto;
       this.mail = mail;
       this.tel = tel;
       this.sucursal = sucursal;
       this.emailFinkok = emailFinkok;
       this.claveFinkok = claveFinkok;
       this.pac = pac;
       this.cer=cer;
       this.llave=llave;
       this.clave=clave;
       this.serie=serie;
       this.serie1=serie1;
       this.serie2=serie2;
       this.version = version;
       this.tipoCambio = tipoCambio;
       this.fechaCambio = fechaCambio;
       this.noProveedor = noProveedor;
    }
   
    public int getIdConfiguracion() {
        return this.idConfiguracion;
    }
    
    public void setIdConfiguracion(int idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getEmpresa() {
        return this.empresa;
    }
    
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    public String getLogo() {
        return this.logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getSello() {
        return this.sello;
    }
    
    public void setSello(String sello) {
        this.sello = sello;
    }
    public String getCinta() {
        return this.cinta;
    }
    
    public void setCinta(String cinta) {
        this.cinta = cinta;
    }
    
    public String getFacturaLogo() {
        return this.facturaLogo;
    }
    
    public void setfacturaLogo(String facturaLogo) {
        this.facturaLogo=facturaLogo;
    }
    
    public Integer getIva() {
        return this.iva;
    }
    
    public void setIva(Integer iva) {
        this.iva = iva;
    }
    public String getRequestor() {
        return this.requestor;
    }
    
    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }
    public String getRfc() {
        return this.rfc;
    }
    
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    public String getUsuario_1() {
        return this.usuario_1;
    }
    
    public void setUsuario_1(String usuario_1) {
        this.usuario_1 = usuario_1;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCp() {
        return this.cp;
    }
    
    public void setCp(String cp) {
        this.cp = cp;
    }
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getNo() {
        return this.no;
    }
    
    public void setNo(String no) {
        this.no = no;
    }
    public String getColonia() {
        return this.colonia;
    }
    
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    public String getMunicipio() {
        return this.municipio;
    }
    
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getPais() {
        return this.pais;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
    }
    public String getContacto() {
        return this.contacto;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    public String getMail() {
        return this.mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public String getTel() {
        return this.tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public String getSucursal() {
        return this.sucursal;
    }
    
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getEmailFinkok() {
        return this.emailFinkok;
    }
    
    public void setEmailFinkok(String emailFinkok) {
        this.emailFinkok = emailFinkok;
    }

    public String getClaveFinkok() {
        return this.claveFinkok;
    }
    
    public void setClaveFinkok(String claveFinkok) {
        this.claveFinkok = claveFinkok;
    }
    
    public String getPac() {
        return this.pac;
    }
    
    public void setPac(String pac) {
        this.pac = pac;
    }
    
    public String getCer() {
        return this.cer;
    }
    
    public void setCer(String cer) {
        this.cer = cer;
    }
    
    public String getLlave() {
        return this.llave;
    }
    
    public void setLlave(String llave) {
        this.llave = llave;
    }
    
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String getSerie() {
        return this.serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public String getSerie1() {
        return this.serie1;
    }
    
    public void setSerie1(String serie1) {
        this.serie1 = serie1;
    }
    
    public String getSerie2() {
        return this.serie2;
    }
    
    public void setSerie2(String serie2) {
        this.serie2 = serie2;
    }
    
    public Double getVersion() {
        return this.version;
    }
    
    public void setVersion(Double version) {
        this.version = version;
    }
    
    public Date getFechaCambio() {
        return this.fechaCambio;
    }
    
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio= fechaCambio;
    }
    
    public Double getTipoCambio() {
        return this.tipoCambio;
    }
    
    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    
     public void setNoProveedor(String noProveedor) {
        this.noProveedor = noProveedor;
    }
    
    public String getNoProveedor() {
        return this.noProveedor;
    }
    
}


