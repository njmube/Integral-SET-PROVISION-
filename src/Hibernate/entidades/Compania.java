package Hibernate.entidades;
// Generated 21/08/2015 05:39:10 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Compania generated by hbm2java
 */
public class Compania  implements java.io.Serializable {


     private Integer idCompania;
     private String nombre;
     private String social;
     private String direccion;
     private String colonia;
     private String poblacion;
     private String estado;
     private String cp;
     private String telefono;
     private String fax;
     private String email;
     private String representante1;
     private String r1Puesto;
     private String representante2;
     private String r2Puesto;
     private Float importeHora;
     private Float importeMax;
     private String tipoPago;
     private Integer plazo;
     private String programaReporte;
     private String comentarios;
     private String grupoEjecutivo;
     private String foto;
     private String rfc;
     private String municipio;
     private String pais;
     private String numeroExterior;
     private String formatoPago;
     private Set ordenExternas = new HashSet(0);
     private Set cuentas = new HashSet(0);
     private Set documentoses = new HashSet(0);
     private Set ordens = new HashSet(0);

    public Compania() {
    }

	
    public Compania(String nombre) {
        this.nombre = nombre;
    }
    public Compania(String nombre, String direccion, String colonia, String poblacion, String estado, String cp, String telefono, String fax, String email, String representante1, String r1Puesto, String representante2, String r2Puesto, Float importeHora, Float importeMax, String tipoPago, Integer plazo, String programaReporte, String comentarios, String grupoEjecutivo, String foto, String rfc, String municipio, String pais, String numeroExterior, String social, String formatoPago, Set ordenExternas, Set cuentas, Set documentoses, Set ordens) {
       this.nombre = nombre;
       this.social = social;
       this.direccion = direccion;
       this.colonia = colonia;
       this.poblacion = poblacion;
       this.estado = estado;
       this.cp = cp;
       this.telefono = telefono;
       this.fax = fax;
       this.email = email;
       this.representante1 = representante1;
       this.r1Puesto = r1Puesto;
       this.representante2 = representante2;
       this.r2Puesto = r2Puesto;
       this.importeHora = importeHora;
       this.importeMax = importeMax;
       this.tipoPago = tipoPago;
       this.plazo = plazo;
       this.programaReporte = programaReporte;
       this.comentarios = comentarios;
       this.grupoEjecutivo = grupoEjecutivo;
       this.foto = foto;
       this.rfc = rfc;
       this.municipio = municipio;
       this.pais = pais;
       this.numeroExterior = numeroExterior;
       this.formatoPago = formatoPago;
       this.ordenExternas = ordenExternas;
       this.cuentas = cuentas;
       this.documentoses = documentoses;
       this.ordens = ordens;
    }
   
    public Integer getIdCompania() {
        return this.idCompania;
    }
    
    public void setIdCompania(Integer idCompania) {
        this.idCompania = idCompania;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getSocial() {
        return this.social;
    }
    
    public void setSocial(String social) {
        this.social = social;
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
    public String getCp() {
        return this.cp;
    }
    
    public void setCp(String cp) {
        this.cp = cp;
    }
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    public String getRepresentante1() {
        return this.representante1;
    }
    
    public void setRepresentante1(String representante1) {
        this.representante1 = representante1;
    }
    public String getR1Puesto() {
        return this.r1Puesto;
    }
    
    public void setR1Puesto(String r1Puesto) {
        this.r1Puesto = r1Puesto;
    }
    public String getRepresentante2() {
        return this.representante2;
    }
    
    public void setRepresentante2(String representante2) {
        this.representante2 = representante2;
    }
    public String getR2Puesto() {
        return this.r2Puesto;
    }
    
    public void setR2Puesto(String r2Puesto) {
        this.r2Puesto = r2Puesto;
    }
    public Float getImporteHora() {
        return this.importeHora;
    }
    
    public void setImporteHora(Float importeHora) {
        this.importeHora = importeHora;
    }
    public Float getImporteMax() {
        return this.importeMax;
    }
    
    public void setImporteMax(Float importeMax) {
        this.importeMax = importeMax;
    }
    public String getTipoPago() {
        return this.tipoPago;
    }
    
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    public Integer getPlazo() {
        return this.plazo;
    }
    
    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }
    public String getProgramaReporte() {
        return this.programaReporte;
    }
    
    public void setProgramaReporte(String programaReporte) {
        this.programaReporte = programaReporte;
    }
    public String getComentarios() {
        return this.comentarios;
    }
    
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    public String getGrupoEjecutivo() {
        return this.grupoEjecutivo;
    }
    
    public void setGrupoEjecutivo(String grupoEjecutivo) {
        this.grupoEjecutivo = grupoEjecutivo;
    }
    public String getFoto() {
        return this.foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getRfc() {
        return this.rfc;
    }
    
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    public String getMunicipio() {
        return this.municipio;
    }
    
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getPais() {
        return this.pais;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
    }
    public String getNumeroExterior() {
        return this.numeroExterior;
    }
    
    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }
    
    public String getFormatoPago() {
        return this.formatoPago;
    }
    
    public void setFormatoPago(String formatoPago) {
        this.formatoPago = formatoPago;
    }
    
    public Set getOrdenExternas() {
        return this.ordenExternas;
    }
    
    public void setOrdenExternas(Set ordenExternas) {
        this.ordenExternas = ordenExternas;
    }
    public Set getCuentas() {
        return this.cuentas;
    }
    
    public void setCuentas(Set cuentas) {
        this.cuentas = cuentas;
    }
    public Set getDocumentoses() {
        return this.documentoses;
    }
    
    public void setDocumentoses(Set documentoses) {
        this.documentoses = documentoses;
    }
    public Set getOrdens() {
        return this.ordens;
    }
    
    public void setOrdens(Set ordens) {
        this.ordens = ordens;
    }

    public boolean eliminaCuenta(Cuenta cue) {
        return this.cuentas.remove(cue);
    }

    public void addCuenta(Cuenta cue) {
        this.cuentas.add(cue);
    }

    public boolean eliminaDocumento(Documentos doc) {
        return this.documentoses.remove(doc);
    }

    public void addDocumento(Documentos doc) {
        this.documentoses.add(doc);
    }



}


