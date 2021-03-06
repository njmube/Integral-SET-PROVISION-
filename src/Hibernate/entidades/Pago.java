package Hibernate.entidades;
// Generated 26/02/2018 04:16:46 PM by Hibernate Tools 3.6.0


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Pago generated by hbm2java
 */
public class Pago  implements java.io.Serializable {


     private Integer idPago;
     private Double version;
     private UsoCfdi usoCfdi;
     private Usuario usuarioByAutorizo;
     private Usuario usuarioByGenero;
     private Date fecha;
     private String rfcEmisor;
     private String nombreEmisor;
     private String calleEmisor;
     private String coloniaEmisor;
     private String numeroExteriorEmisor;
     private String municipioEmisor;
     private String estadoEmisor;
     private String paisEmisor;
     private String cpEmisor;
     private String rfcReceptor;
     private String nombreReceptor;
     private String calleReceptor;
     private String coloniaReceptor;
     private String numeroExteriorReceptor;
     private String municipioReceptor;
     private String estadoReceptor;
     private String paisReceptor;
     private String cpReceptor;
     private String estadoFactura;
     private String FFiscal;
     private String fechaFiscal;
     private String serie;
     private Integer folio;
     private String metodoPago;
     private String moneda;
     private String nombreDocumento;
     private Double monto;
     private Date fechaPago;
     private String estatus;
     private Date FEstatus;
     private String selloSat;
     private String selloCfdi;
     private String certificadoSat;
     private String certificadoEmisor;
     private String tipoRelacion;
     private String correoReceptor;
     private String formaPago;
     private String referencia;
     private Set documentoPagos = new HashSet(0);
     private Set relacionsForIdPago = new HashSet(0);
     private Set relacionsForRelacionPago = new HashSet(0);

    public Pago() {
    }

    public Pago(UsoCfdi usoCfdi, Usuario usuarioByAutorizo, Usuario usuarioByGenero, Date fecha, String rfcEmisor, String nombreEmisor, String calleEmisor, String coloniaEmisor, String numeroExteriorEmisor, String municipioEmisor, String estadoEmisor, String paisEmisor, String cpEmisor, String rfcReceptor, String nombreReceptor, String calleReceptor, String coloniaReceptor, String numeroExteriorReceptor, String municipioReceptor, String estadoReceptor, String paisReceptor, String cpReceptor, String estadoFactura, String FFiscal, String fechaFiscal, String serie, Integer folio, String metodoPago, String moneda, Double factorCambio, String nombreDocumento, Double monto, Date fechaPago, String estatus, Date FEstatus, String selloSat, String selloCfdi, String certificadoSat, String certificadoEmisor, String tipoRelacion, String correoReceptor, String formaPago, String referencia, Set documentoPagos, Set relacionsForIdPago, Set relacionsForRelacionPago) {
       this.usoCfdi = usoCfdi;
       this.usuarioByAutorizo = usuarioByAutorizo;
       this.usuarioByGenero = usuarioByGenero;
       this.fecha = fecha;
       this.rfcEmisor = rfcEmisor;
       this.nombreEmisor = nombreEmisor;
       this.calleEmisor = calleEmisor;
       this.coloniaEmisor = coloniaEmisor;
       this.numeroExteriorEmisor = numeroExteriorEmisor;
       this.municipioEmisor = municipioEmisor;
       this.estadoEmisor = estadoEmisor;
       this.paisEmisor = paisEmisor;
       this.cpEmisor = cpEmisor;
       this.rfcReceptor = rfcReceptor;
       this.nombreReceptor = nombreReceptor;
       this.calleReceptor = calleReceptor;
       this.coloniaReceptor = coloniaReceptor;
       this.numeroExteriorReceptor = numeroExteriorReceptor;
       this.municipioReceptor = municipioReceptor;
       this.estadoReceptor = estadoReceptor;
       this.paisReceptor = paisReceptor;
       this.cpReceptor = cpReceptor;
       this.estadoFactura = estadoFactura;
       this.FFiscal = FFiscal;
       this.fechaFiscal = fechaFiscal;
       this.serie = serie;
       this.folio = folio;
       this.metodoPago = metodoPago;
       this.moneda = moneda;
       this.nombreDocumento = nombreDocumento;
       this.monto = monto;
       this.fechaPago = fechaPago;
       this.estatus = estatus;
       this.FEstatus = FEstatus;
       this.selloSat = selloSat;
       this.selloCfdi = selloCfdi;
       this.certificadoSat = certificadoSat;
       this.certificadoEmisor = certificadoEmisor;
       this.tipoRelacion = tipoRelacion;
       this.correoReceptor = correoReceptor;
       this.formaPago = formaPago;
       this.referencia = referencia;
       this.documentoPagos = documentoPagos;
       this.relacionsForIdPago = relacionsForIdPago;
       this.relacionsForRelacionPago = relacionsForRelacionPago;
    }
   
    public Integer getIdPago() {
        return this.idPago;
    }
    
    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }
    public Double getVersion() {
        return this.version;
    }
    
    public void setVersion(Double version) {
        this.version = version;
    }
    public UsoCfdi getUsoCfdi() {
        return this.usoCfdi;
    }
    
    public void setUsoCfdi(UsoCfdi usoCfdi) {
        this.usoCfdi = usoCfdi;
    }
    public Usuario getUsuarioByAutorizo() {
        return this.usuarioByAutorizo;
    }
    
    public void setUsuarioByAutorizo(Usuario usuarioByAutorizo) {
        this.usuarioByAutorizo = usuarioByAutorizo;
    }
    public Usuario getUsuarioByGenero() {
        return this.usuarioByGenero;
    }
    
    public void setUsuarioByGenero(Usuario usuarioByGenero) {
        this.usuarioByGenero = usuarioByGenero;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getRfcEmisor() {
        return this.rfcEmisor;
    }
    
    public void setRfcEmisor(String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }
    public String getNombreEmisor() {
        return this.nombreEmisor;
    }
    
    public void setNombreEmisor(String nombreEmisor) {
        this.nombreEmisor = nombreEmisor;
    }
    public String getCalleEmisor() {
        return this.calleEmisor;
    }
    
    public void setCalleEmisor(String calleEmisor) {
        this.calleEmisor = calleEmisor;
    }
    public String getColoniaEmisor() {
        return this.coloniaEmisor;
    }
    
    public void setColoniaEmisor(String coloniaEmisor) {
        this.coloniaEmisor = coloniaEmisor;
    }
    public String getNumeroExteriorEmisor() {
        return this.numeroExteriorEmisor;
    }
    
    public void setNumeroExteriorEmisor(String numeroExteriorEmisor) {
        this.numeroExteriorEmisor = numeroExteriorEmisor;
    }
    public String getMunicipioEmisor() {
        return this.municipioEmisor;
    }
    
    public void setMunicipioEmisor(String municipioEmisor) {
        this.municipioEmisor = municipioEmisor;
    }
    public String getEstadoEmisor() {
        return this.estadoEmisor;
    }
    
    public void setEstadoEmisor(String estadoEmisor) {
        this.estadoEmisor = estadoEmisor;
    }
    public String getPaisEmisor() {
        return this.paisEmisor;
    }
    
    public void setPaisEmisor(String paisEmisor) {
        this.paisEmisor = paisEmisor;
    }
    public String getCpEmisor() {
        return this.cpEmisor;
    }
    
    public void setCpEmisor(String cpEmisor) {
        this.cpEmisor = cpEmisor;
    }
    public String getRfcReceptor() {
        return this.rfcReceptor;
    }
    
    public void setRfcReceptor(String rfcReceptor) {
        this.rfcReceptor = rfcReceptor;
    }
    public String getNombreReceptor() {
        return this.nombreReceptor;
    }
    
    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }
    public String getCalleReceptor() {
        return this.calleReceptor;
    }
    
    public void setCalleReceptor(String calleReceptor) {
        this.calleReceptor = calleReceptor;
    }
    public String getColoniaReceptor() {
        return this.coloniaReceptor;
    }
    
    public void setColoniaReceptor(String coloniaReceptor) {
        this.coloniaReceptor = coloniaReceptor;
    }
    public String getNumeroExteriorReceptor() {
        return this.numeroExteriorReceptor;
    }
    
    public void setNumeroExteriorReceptor(String numeroExteriorReceptor) {
        this.numeroExteriorReceptor = numeroExteriorReceptor;
    }
    public String getMunicipioReceptor() {
        return this.municipioReceptor;
    }
    
    public void setMunicipioReceptor(String municipioReceptor) {
        this.municipioReceptor = municipioReceptor;
    }
    public String getEstadoReceptor() {
        return this.estadoReceptor;
    }
    
    public void setEstadoReceptor(String estadoReceptor) {
        this.estadoReceptor = estadoReceptor;
    }
    public String getPaisReceptor() {
        return this.paisReceptor;
    }
    
    public void setPaisReceptor(String paisReceptor) {
        this.paisReceptor = paisReceptor;
    }
    public String getCpReceptor() {
        return this.cpReceptor;
    }
    
    public void setCpReceptor(String cpReceptor) {
        this.cpReceptor = cpReceptor;
    }
    public String getEstadoFactura() {
        return this.estadoFactura;
    }
    
    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }
    public String getFFiscal() {
        return this.FFiscal;
    }
    
    public void setFFiscal(String FFiscal) {
        this.FFiscal = FFiscal;
    }
    public String getFechaFiscal() {
        return this.fechaFiscal;
    }
    
    public void setFechaFiscal(String fechaFiscal) {
        this.fechaFiscal = fechaFiscal;
    }
    public String getSerie() {
        return this.serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public Integer getFolio() {
        return this.folio;
    }
    
    public void setFolio(Integer folio) {
        this.folio = folio;
    }
    public String getMetodoPago() {
        return this.metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    public String getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    
    public String getNombreDocumento() {
        return this.nombreDocumento;
    }
    
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
    
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    public Date getFechaPago() {
        return this.fechaPago;
    }
    
    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }
    public String getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    public Date getFEstatus() {
        return this.FEstatus;
    }
    
    public void setFEstatus(Date FEstatus) {
        this.FEstatus = FEstatus;
    }
    public String getSelloSat() {
        return this.selloSat;
    }
    
    public void setSelloSat(String selloSat) {
        this.selloSat = selloSat;
    }
    public String getSelloCfdi() {
        return this.selloCfdi;
    }
    
    public void setSelloCfdi(String selloCfdi) {
        this.selloCfdi = selloCfdi;
    }
    public String getCertificadoSat() {
        return this.certificadoSat;
    }
    
    public void setCertificadoSat(String certificadoSat) {
        this.certificadoSat = certificadoSat;
    }
    public String getCertificadoEmisor() {
        return this.certificadoEmisor;
    }
    
    public void setCertificadoEmisor(String certificadoEmisor) {
        this.certificadoEmisor = certificadoEmisor;
    }
    public String getTipoRelacion() {
        return this.tipoRelacion;
    }
    
    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    public Set getDocumentoPagos() {
        return this.documentoPagos;
    }
    
    public void setDocumentoPagos(Set documentoPagos) {
        this.documentoPagos = documentoPagos;
    }

    public String getCorreoReceptor() {
        return this.correoReceptor;
    }
    
    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    public String getFormaPago() {
        return this.formaPago;
    }
    
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getReferencia() {
        return this.referencia;
    }
    
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    public Set getRelacionsForIdPago() {
        return this.relacionsForIdPago;
    }
    
    public void setRelacionsForIdPago(Set relacionsForIdPago) {
        this.relacionsForIdPago = relacionsForIdPago;
    }
    public Set getRelacionsForRelacionPago() {
        return this.relacionsForRelacionPago;
    }
    
    public void setRelacionsForRelacionPago(Set relacionsForRelacionFactura) {
        this.relacionsForRelacionPago = relacionsForRelacionPago;
    }
    
}


