package Hibernate.entidades;
// Generated 9/09/2015 03:17:33 PM by Hibernate Tools 3.6.0


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Orden generated by hbm2java
 */
public class Orden  implements java.io.Serializable {


     private int idOrden;
     private Usuario usuarioByREntregarAsigno;
     private Reparacion reparacion;
     private Usuario usuarioByIdModificado;
     private Marca marca;
     private Usuario usuarioByRAutorizacionInternaAsigno;
     private Compania compania;
     private Usuario usuarioByRCierre;
     private Estatus estatus;
     private Usuario usuarioByIdUserApertura;
     private Empleado empleadoByRMecanica;
     private Usuario usuarioByRElectricoAsigno;
     private Usuario usuarioByRValuacionCierreAsigno;
     private Usuario usuarioByAutorizaCompaniaAsigno;
     private Usuario usuarioByRLevantamientoAsigno;
     private Usuario usuarioByRExpedienteAsigno;
     private Usuario usuarioByRRefaccionesAsigno;
     private Empleado empleadoByRRefacciones;
     private Usuario usuarioByRCotizaAsigno;
     private Empleado empleadoByRElectrico;
     private Tipo tipo;
     private Empleado empleadoByRValuacion;
     private Empleado empleadoByRHojalateria;
     private Empleado empleadoByRCotiza;
     private Empleado empleadoByRLevantamiento;
     private Usuario usuarioByAutorizaClienteAsigno;
     private Usuario usuarioByEnvioCompaniaAsigno;
     private Clientes clientes;
     private Usuario usuarioByRHojalateriaAsigno;
     private Usuario usuarioByRFactura;
     private Usuario usuarioByRSuspensionAsigno;
     private Usuario usuarioByBloqueada;
     private Usuario usuarioByRMecanicaAsigno;
     private Empleado empleadoByRSuspension;
     private Usuario usuarioByRValuacionAsigno;
     private Usuario usuarioByRReparacionInicioAsigno;
     private Empleado empleadoByRExpediente;
     private Ciclo ciclo;
     private Date fecha;
     private Date hora;
     private String siniestro;
     private String poliza;
     private String noReporte;
     private Date fechaSiniestro;
     private String inciso;
     private String noMotor;
     private String noPlacas;
     private Integer modelo;
     private String noSerie;
     private String noEconomico;
     private Double deducible;
     private Double demerito;
     private String tipoCliente;
     private Date fechaEstatus;
     private Date fechaTaller;
     private Date fechaCliente;
     private Date RExpedienteFecha;
     private Date RLevantamientoInicio;
     private Date RLevantamientoCierre;
     private Date RCotizaInicio;
     private Date RCotizaCierre;
     private Date autorizaCliente;
     private Date RAutorizacionInterna;
     private Date RValuacionInicio;
     private Date envioCompania;
     private Date autorizaCompania;
     private Date RValuacionCierre;
     private Date metaValuacion;
     private Date inicioRefacciones;
     private Date cierreRefacciones;
     private Date metaRefacciones;
     private Date RReparacionInicio;
     private Date RHojalateriaFecha;
     private Date RMecanicaFecha;
     private Date RSuspensionFecha;
     private Date RElectricoFecha;
     private Date RReparacionCierre;
     private Date metaReparacion;
     private Date REntregarFecha;
     private Integer noFactura;
     private Date fehaFectura;
     private Date fechaCierre;
     private Integer idObservacion;
     private String session;
     private String ventana;
     private Double refPresupuesto;
     private Double refAutorizadas;
     private Double moPresupuestada;
     private Double moDirecta;
     private Boolean autorizadoFacturar;
     private String color;
     private String propietario;
     private String codigoAsegurado;
     private String tipoVehiculo;
     private String contratante;
     private String Km;
     private Double vales;
     private Empleado empleadoByRPintura;
     private Date RPinturaFecha;
     private Usuario usuarioByRPinturaAsigno;
     private Double importeHojalateria;
     private Double importeMecanica;
     private Double importeSuspension;
     private Double importeElectrico;
     private Double importePintura;
     private Date hojalateriaLimite;
     private Date mecanicaLimite;
     private Date suspensionLimite;
     private Date electricoLimite;
     private Date pinturaLimite;
     private Set archivos = new HashSet(0);
     private Set partidasForEnlazada = new HashSet(0);
     private Set notas = new HashSet(0);
     private Set adicionaleses = new HashSet(0);
     private Set agendas = new HashSet(0);
     private Set destajos = new HashSet(0);
     private Set pagoAdicional = new HashSet(0);
     private Set inventarios = new HashSet(0);
     private Set facturas = new HashSet(0);
     private Set fotos = new HashSet(0);
     private Set mensajes = new HashSet(0);
     private Set partidasForIdOrden = new HashSet(0);
     private Set pedidos = new HashSet(0);

    public Orden() {
    }

	
    public Orden(int idOrden, Marca marca, Estatus estatus, Tipo tipo, Clientes clientes, Date fecha) {
        this.idOrden = idOrden;
        this.marca = marca;
        this.estatus = estatus;
        this.tipo = tipo;
        this.clientes = clientes;
        this.fecha = fecha;
    }
    public Orden(int idOrden, Usuario usuarioByREntregarAsigno, Reparacion reparacion, Usuario usuarioByIdModificado, Marca marca, Usuario usuarioByRAutorizacionInternaAsigno, Compania compania, Usuario usuarioByRCierre, Estatus estatus, Usuario usuarioByIdUserApertura, Empleado empleadoByRMecanica, Usuario usuarioByRElectricoAsigno, Usuario usuarioByRValuacionCierreAsigno, Usuario usuarioByAutorizaCompaniaAsigno, Usuario usuarioByRLevantamientoAsigno, Usuario usuarioByRExpedienteAsigno, Usuario usuarioByRRefaccionesAsigno, Empleado empleadoByRRefacciones, Usuario usuarioByRCotizaAsigno, Empleado empleadoByRElectrico, Tipo tipo, Empleado empleadoByRValuacion, Empleado empleadoByRHojalateria, Empleado empleadoByRCotiza, Empleado empleadoByRLevantamiento, Usuario usuarioByAutorizaClienteAsigno, Usuario usuarioByEnvioCompaniaAsigno, Clientes clientes, Usuario usuarioByRHojalateriaAsigno, Usuario usuarioByRFactura, Usuario usuarioByRSuspensionAsigno, Usuario usuarioByBloqueada, Usuario usuarioByRMecanicaAsigno, Empleado empleadoByRSuspension, Usuario usuarioByRValuacionAsigno, Usuario usuarioByRReparacionInicioAsigno, Empleado empleadoByRExpediente, Ciclo ciclo, Date fecha, Date hora, String siniestro, String poliza, String noReporte, Date fechaSiniestro, String inciso, String noMotor, String noPlacas, Integer modelo, String noSerie, String noEconomico, Double deducible, Double demerito, String tipoCliente, Date fechaEstatus, Date fechaTaller, Date fechaCliente, Date RExpedienteFecha, Date RLevantamientoInicio, Date RLevantamientoCierre, Date RCotizaInicio, Date RCotizaCierre, Date autorizaCliente, Date RAutorizacionInterna, Date RValuacionInicio, Date envioCompania, Date autorizaCompania, Date RValuacionCierre, Date metaValuacion, Date inicioRefacciones, Date cierreRefacciones, Date metaRefacciones, Date RReparacionInicio, Date RHojalateriaFecha, Date RMecanicaFecha, Date RSuspensionFecha, Date RElectricoFecha, Date RReparacionCierre, Date metaReparacion, Date REntregarFecha, Integer noFactura, Date fehaFectura, Date fechaCierre, Integer idObservacion, String session, String ventana, Double refPresupuesto, Double refAutorizadas, Double moPresupuestada, Double moDirecta, Boolean autorizadoFacturar, String color, String propietario, String codigoAsegurado, String tipoVehiculo, String contratante, String Km, double vales, Empleado empleadoByRPintura, Date RPinturaFecha, Usuario usuarioByRPinturaAsigno, Double importeHojalateria, Double importeMecanica, Double importeSuspension, Double importeElectrico, Double importePintura, Date hojalateriaLimite, Date mecanicaLimite, Date suspensionLimite, Date electricoLimite, Date pinturaLimite, Set archivos, Set partidasForEnlazada, Set notas, Set adicionaleses, Set agendas, Set destajos, Set pagoAdicional, Set inventarios, Set facturas, Set fotos, Set mensajes, Set partidasForIdOrden, Set pedidos) {
       this.idOrden = idOrden;
       this.usuarioByREntregarAsigno = usuarioByREntregarAsigno;
       this.reparacion = reparacion;
       this.usuarioByIdModificado = usuarioByIdModificado;
       this.marca = marca;
       this.usuarioByRAutorizacionInternaAsigno = usuarioByRAutorizacionInternaAsigno;
       this.compania = compania;
       this.usuarioByRCierre = usuarioByRCierre;
       this.estatus = estatus;
       this.usuarioByIdUserApertura = usuarioByIdUserApertura;
       this.empleadoByRMecanica = empleadoByRMecanica;
       this.usuarioByRElectricoAsigno = usuarioByRElectricoAsigno;
       this.usuarioByRValuacionCierreAsigno = usuarioByRValuacionCierreAsigno;
       this.usuarioByAutorizaCompaniaAsigno = usuarioByAutorizaCompaniaAsigno;
       this.usuarioByRLevantamientoAsigno = usuarioByRLevantamientoAsigno;
       this.usuarioByRExpedienteAsigno = usuarioByRExpedienteAsigno;
       this.usuarioByRRefaccionesAsigno = usuarioByRRefaccionesAsigno;
       this.empleadoByRRefacciones = empleadoByRRefacciones;
       this.usuarioByRCotizaAsigno = usuarioByRCotizaAsigno;
       this.empleadoByRElectrico = empleadoByRElectrico;
       this.tipo = tipo;
       this.empleadoByRValuacion = empleadoByRValuacion;
       this.empleadoByRHojalateria = empleadoByRHojalateria;
       this.empleadoByRCotiza = empleadoByRCotiza;
       this.empleadoByRLevantamiento = empleadoByRLevantamiento;
       this.usuarioByAutorizaClienteAsigno = usuarioByAutorizaClienteAsigno;
       this.usuarioByEnvioCompaniaAsigno = usuarioByEnvioCompaniaAsigno;
       this.clientes = clientes;
       this.usuarioByRHojalateriaAsigno = usuarioByRHojalateriaAsigno;
       this.usuarioByRFactura = usuarioByRFactura;
       this.usuarioByRSuspensionAsigno = usuarioByRSuspensionAsigno;
       this.usuarioByBloqueada = usuarioByBloqueada;
       this.usuarioByRMecanicaAsigno = usuarioByRMecanicaAsigno;
       this.empleadoByRSuspension = empleadoByRSuspension;
       this.usuarioByRValuacionAsigno = usuarioByRValuacionAsigno;
       this.usuarioByRReparacionInicioAsigno = usuarioByRReparacionInicioAsigno;
       this.empleadoByRExpediente = empleadoByRExpediente;
       this.ciclo = ciclo;
       this.fecha = fecha;
       this.hora = hora;
       this.siniestro = siniestro;
       this.poliza = poliza;
       this.noReporte = noReporte;
       this.fechaSiniestro = fechaSiniestro;
       this.inciso = inciso;
       this.noMotor = noMotor;
       this.noPlacas = noPlacas;
       this.modelo = modelo;
       this.noSerie = noSerie;
       this.noEconomico = noEconomico;
       this.deducible = deducible;
       this.demerito = demerito;
       this.tipoCliente = tipoCliente;
       this.fechaEstatus = fechaEstatus;
       this.fechaTaller = fechaTaller;
       this.fechaCliente = fechaCliente;
       this.RExpedienteFecha = RExpedienteFecha;
       this.RLevantamientoInicio = RLevantamientoInicio;
       this.RLevantamientoCierre = RLevantamientoCierre;
       this.RCotizaInicio = RCotizaInicio;
       this.RCotizaCierre = RCotizaCierre;
       this.autorizaCliente = autorizaCliente;
       this.RAutorizacionInterna = RAutorizacionInterna;
       this.RValuacionInicio = RValuacionInicio;
       this.envioCompania = envioCompania;
       this.autorizaCompania = autorizaCompania;
       this.RValuacionCierre = RValuacionCierre;
       this.metaValuacion = metaValuacion;
       this.inicioRefacciones = inicioRefacciones;
       this.cierreRefacciones = cierreRefacciones;
       this.metaRefacciones = metaRefacciones;
       this.RReparacionInicio = RReparacionInicio;
       this.RHojalateriaFecha = RHojalateriaFecha;
       this.RMecanicaFecha = RMecanicaFecha;
       this.RSuspensionFecha = RSuspensionFecha;
       this.RElectricoFecha = RElectricoFecha;
       this.RReparacionCierre = RReparacionCierre;
       this.metaReparacion = metaReparacion;
       this.REntregarFecha = REntregarFecha;
       this.noFactura = noFactura;
       this.fehaFectura = fehaFectura;
       this.fechaCierre = fechaCierre;
       this.idObservacion = idObservacion;
       this.session = session;
       this.ventana = ventana;
       this.refPresupuesto = refPresupuesto;
       this.refAutorizadas = refAutorizadas;
       this.moPresupuestada = moPresupuestada;
       this.moDirecta = moDirecta;
       this.autorizadoFacturar = autorizadoFacturar;
       this.color = color;
       this.propietario = propietario;
       this.codigoAsegurado = codigoAsegurado;
       this.tipoVehiculo = tipoVehiculo;
       this.contratante = contratante;
       this.Km = Km;
       this.vales = vales;
       this.empleadoByRPintura = empleadoByRPintura;
       this.RPinturaFecha = RPinturaFecha;
       this.usuarioByRPinturaAsigno = usuarioByRPinturaAsigno;
       this.importeHojalateria=importeHojalateria;
       this.importeMecanica=importeMecanica;
       this.importeSuspension=importeSuspension;
       this.importeElectrico=importeElectrico;
       this.importePintura=importePintura;
       this.hojalateriaLimite=hojalateriaLimite;
       this.mecanicaLimite=mecanicaLimite;
       this.suspensionLimite=suspensionLimite;
       this.electricoLimite=electricoLimite;
       this.pinturaLimite=pinturaLimite;
       this.archivos = archivos;
       this.partidasForEnlazada = partidasForEnlazada;
       this.notas = notas;
       this.adicionaleses = adicionaleses;
       this.agendas = agendas;
       this.destajos = destajos;
       this.pagoAdicional = pagoAdicional;
       this.inventarios = inventarios;
       this.facturas = facturas;
       this.fotos = fotos;
       this.mensajes = mensajes;
       this.partidasForIdOrden = partidasForIdOrden;
       this.pedidos= pedidos;
    }
   
    public int getIdOrden() {
        return this.idOrden;
    }
    
    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
    public Usuario getUsuarioByREntregarAsigno() {
        return this.usuarioByREntregarAsigno;
    }
    
    public void setUsuarioByREntregarAsigno(Usuario usuarioByREntregarAsigno) {
        this.usuarioByREntregarAsigno = usuarioByREntregarAsigno;
    }
    public Reparacion getReparacion() {
        return this.reparacion;
    }
    
    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }
    public Usuario getUsuarioByIdModificado() {
        return this.usuarioByIdModificado;
    }
    
    public void setUsuarioByIdModificado(Usuario usuarioByIdModificado) {
        this.usuarioByIdModificado = usuarioByIdModificado;
    }
    public Marca getMarca() {
        return this.marca;
    }
    
    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    public Usuario getUsuarioByRAutorizacionInternaAsigno() {
        return this.usuarioByRAutorizacionInternaAsigno;
    }
    
    public void setUsuarioByRAutorizacionInternaAsigno(Usuario usuarioByRAutorizacionInternaAsigno) {
        this.usuarioByRAutorizacionInternaAsigno = usuarioByRAutorizacionInternaAsigno;
    }
    public Compania getCompania() {
        return this.compania;
    }
    
    public void setCompania(Compania compania) {
        this.compania = compania;
    }
    public Usuario getUsuarioByRCierre() {
        return this.usuarioByRCierre;
    }
    
    public void setUsuarioByRCierre(Usuario usuarioByRCierre) {
        this.usuarioByRCierre = usuarioByRCierre;
    }
    public Estatus getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }
    public Usuario getUsuarioByIdUserApertura() {
        return this.usuarioByIdUserApertura;
    }
    
    public void setUsuarioByIdUserApertura(Usuario usuarioByIdUserApertura) {
        this.usuarioByIdUserApertura = usuarioByIdUserApertura;
    }
    public Empleado getEmpleadoByRMecanica() {
        return this.empleadoByRMecanica;
    }
    
    public void setEmpleadoByRMecanica(Empleado empleadoByRMecanica) {
        this.empleadoByRMecanica = empleadoByRMecanica;
    }
    public Usuario getUsuarioByRElectricoAsigno() {
        return this.usuarioByRElectricoAsigno;
    }
    
    public void setUsuarioByRElectricoAsigno(Usuario usuarioByRElectricoAsigno) {
        this.usuarioByRElectricoAsigno = usuarioByRElectricoAsigno;
    }
    public Usuario getUsuarioByRValuacionCierreAsigno() {
        return this.usuarioByRValuacionCierreAsigno;
    }
    
    public void setUsuarioByRValuacionCierreAsigno(Usuario usuarioByRValuacionCierreAsigno) {
        this.usuarioByRValuacionCierreAsigno = usuarioByRValuacionCierreAsigno;
    }
    public Usuario getUsuarioByAutorizaCompaniaAsigno() {
        return this.usuarioByAutorizaCompaniaAsigno;
    }
    
    public void setUsuarioByAutorizaCompaniaAsigno(Usuario usuarioByAutorizaCompaniaAsigno) {
        this.usuarioByAutorizaCompaniaAsigno = usuarioByAutorizaCompaniaAsigno;
    }
    public Usuario getUsuarioByRLevantamientoAsigno() {
        return this.usuarioByRLevantamientoAsigno;
    }
    
    public void setUsuarioByRLevantamientoAsigno(Usuario usuarioByRLevantamientoAsigno) {
        this.usuarioByRLevantamientoAsigno = usuarioByRLevantamientoAsigno;
    }
    public Usuario getUsuarioByRExpedienteAsigno() {
        return this.usuarioByRExpedienteAsigno;
    }
    
    public void setUsuarioByRExpedienteAsigno(Usuario usuarioByRExpedienteAsigno) {
        this.usuarioByRExpedienteAsigno = usuarioByRExpedienteAsigno;
    }
    public Usuario getUsuarioByRRefaccionesAsigno() {
        return this.usuarioByRRefaccionesAsigno;
    }
    
    public void setUsuarioByRRefaccionesAsigno(Usuario usuarioByRRefaccionesAsigno) {
        this.usuarioByRRefaccionesAsigno = usuarioByRRefaccionesAsigno;
    }
    public Empleado getEmpleadoByRRefacciones() {
        return this.empleadoByRRefacciones;
    }
    
    public void setEmpleadoByRRefacciones(Empleado empleadoByRRefacciones) {
        this.empleadoByRRefacciones = empleadoByRRefacciones;
    }
    public Usuario getUsuarioByRCotizaAsigno() {
        return this.usuarioByRCotizaAsigno;
    }
    
    public void setUsuarioByRCotizaAsigno(Usuario usuarioByRCotizaAsigno) {
        this.usuarioByRCotizaAsigno = usuarioByRCotizaAsigno;
    }
    public Empleado getEmpleadoByRElectrico() {
        return this.empleadoByRElectrico;
    }
    
    public void setEmpleadoByRElectrico(Empleado empleadoByRElectrico) {
        this.empleadoByRElectrico = empleadoByRElectrico;
    }
    public Tipo getTipo() {
        return this.tipo;
    }
    
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    public Empleado getEmpleadoByRValuacion() {
        return this.empleadoByRValuacion;
    }
    
    public void setEmpleadoByRValuacion(Empleado empleadoByRValuacion) {
        this.empleadoByRValuacion = empleadoByRValuacion;
    }
    public Empleado getEmpleadoByRHojalateria() {
        return this.empleadoByRHojalateria;
    }
    
    public void setEmpleadoByRHojalateria(Empleado empleadoByRHojalateria) {
        this.empleadoByRHojalateria = empleadoByRHojalateria;
    }
    public Empleado getEmpleadoByRCotiza() {
        return this.empleadoByRCotiza;
    }
    
    public void setEmpleadoByRCotiza(Empleado empleadoByRCotiza) {
        this.empleadoByRCotiza = empleadoByRCotiza;
    }
    public Empleado getEmpleadoByRLevantamiento() {
        return this.empleadoByRLevantamiento;
    }
    
    public void setEmpleadoByRLevantamiento(Empleado empleadoByRLevantamiento) {
        this.empleadoByRLevantamiento = empleadoByRLevantamiento;
    }
    public Usuario getUsuarioByAutorizaClienteAsigno() {
        return this.usuarioByAutorizaClienteAsigno;
    }
    
    public void setUsuarioByAutorizaClienteAsigno(Usuario usuarioByAutorizaClienteAsigno) {
        this.usuarioByAutorizaClienteAsigno = usuarioByAutorizaClienteAsigno;
    }
    public Usuario getUsuarioByEnvioCompaniaAsigno() {
        return this.usuarioByEnvioCompaniaAsigno;
    }
    
    public void setUsuarioByEnvioCompaniaAsigno(Usuario usuarioByEnvioCompaniaAsigno) {
        this.usuarioByEnvioCompaniaAsigno = usuarioByEnvioCompaniaAsigno;
    }
    public Clientes getClientes() {
        return this.clientes;
    }
    
    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }
    public Usuario getUsuarioByRHojalateriaAsigno() {
        return this.usuarioByRHojalateriaAsigno;
    }
    
    public void setUsuarioByRHojalateriaAsigno(Usuario usuarioByRHojalateriaAsigno) {
        this.usuarioByRHojalateriaAsigno = usuarioByRHojalateriaAsigno;
    }
    public Usuario getUsuarioByRFactura() {
        return this.usuarioByRFactura;
    }
    
    public void setUsuarioByRFactura(Usuario usuarioByRFactura) {
        this.usuarioByRFactura = usuarioByRFactura;
    }
    public Usuario getUsuarioByRSuspensionAsigno() {
        return this.usuarioByRSuspensionAsigno;
    }
    
    public void setUsuarioByRSuspensionAsigno(Usuario usuarioByRSuspensionAsigno) {
        this.usuarioByRSuspensionAsigno = usuarioByRSuspensionAsigno;
    }
    public Usuario getUsuarioByBloqueada() {
        return this.usuarioByBloqueada;
    }
    
    public void setUsuarioByBloqueada(Usuario usuarioByBloqueada) {
        this.usuarioByBloqueada = usuarioByBloqueada;
    }
    public Usuario getUsuarioByRMecanicaAsigno() {
        return this.usuarioByRMecanicaAsigno;
    }
    
    public void setUsuarioByRMecanicaAsigno(Usuario usuarioByRMecanicaAsigno) {
        this.usuarioByRMecanicaAsigno = usuarioByRMecanicaAsigno;
    }
    public Empleado getEmpleadoByRSuspension() {
        return this.empleadoByRSuspension;
    }
    
    public void setEmpleadoByRSuspension(Empleado empleadoByRSuspension) {
        this.empleadoByRSuspension = empleadoByRSuspension;
    }
    public Usuario getUsuarioByRValuacionAsigno() {
        return this.usuarioByRValuacionAsigno;
    }
    
    public void setUsuarioByRValuacionAsigno(Usuario usuarioByRValuacionAsigno) {
        this.usuarioByRValuacionAsigno = usuarioByRValuacionAsigno;
    }
    public Usuario getUsuarioByRReparacionInicioAsigno() {
        return this.usuarioByRReparacionInicioAsigno;
    }
    
    public void setUsuarioByRReparacionInicioAsigno(Usuario usuarioByRReparacionInicioAsigno) {
        this.usuarioByRReparacionInicioAsigno = usuarioByRReparacionInicioAsigno;
    }
    public Empleado getEmpleadoByRExpediente() {
        return this.empleadoByRExpediente;
    }
    
    public void setEmpleadoByRExpediente(Empleado empleadoByRExpediente) {
        this.empleadoByRExpediente = empleadoByRExpediente;
    }
    public Ciclo getCiclo() {
        return this.ciclo;
    }
    
    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Date getHora() {
        return this.hora;
    }
    
    public void setHora(Date hora) {
        this.hora = hora;
    }
    public String getSiniestro() {
        return this.siniestro;
    }
    
    public void setSiniestro(String siniestro) {
        this.siniestro = siniestro;
    }
    public String getPoliza() {
        return this.poliza;
    }
    
    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }
    public String getNoReporte() {
        return this.noReporte;
    }
    
    public void setNoReporte(String noReporte) {
        this.noReporte = noReporte;
    }
    public Date getFechaSiniestro() {
        return this.fechaSiniestro;
    }
    
    public void setFechaSiniestro(Date fechaSiniestro) {
        this.fechaSiniestro = fechaSiniestro;
    }
    public String getInciso() {
        return this.inciso;
    }
    
    public void setInciso(String inciso) {
        this.inciso = inciso;
    }
    public String getNoMotor() {
        return this.noMotor;
    }
    
    public void setNoMotor(String noMotor) {
        this.noMotor = noMotor;
    }
    public String getNoPlacas() {
        return this.noPlacas;
    }
    
    public void setNoPlacas(String noPlacas) {
        this.noPlacas = noPlacas;
    }
    public Integer getModelo() {
        return this.modelo;
    }
    
    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }
    public String getNoSerie() {
        return this.noSerie;
    }
    
    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }
    public String getNoEconomico() {
        return this.noEconomico;
    }
    
    public void setNoEconomico(String noEconomico) {
        this.noEconomico = noEconomico;
    }
    public Double getDeducible() {
        return this.deducible;
    }
    
    public void setDeducible(Double deducible) {
        this.deducible = deducible;
    }
    public Double getDemerito() {
        return this.demerito;
    }
    
    public void setDemerito(Double demerito) {
        this.demerito = demerito;
    }
    public String getTipoCliente() {
        return this.tipoCliente;
    }
    
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public Date getFechaEstatus() {
        return this.fechaEstatus;
    }
    
    public void setFechaEstatus(Date fechaEstatus) {
        this.fechaEstatus = fechaEstatus;
    }
    public Date getFechaTaller() {
        return this.fechaTaller;
    }
    
    public void setFechaTaller(Date fechaTaller) {
        this.fechaTaller = fechaTaller;
    }
    public Date getFechaCliente() {
        return this.fechaCliente;
    }
    
    public void setFechaCliente(Date fechaCliente) {
        this.fechaCliente = fechaCliente;
    }
    public Date getRExpedienteFecha() {
        return this.RExpedienteFecha;
    }
    
    public void setRExpedienteFecha(Date RExpedienteFecha) {
        this.RExpedienteFecha = RExpedienteFecha;
    }
    public Date getRLevantamientoInicio() {
        return this.RLevantamientoInicio;
    }
    
    public void setRLevantamientoInicio(Date RLevantamientoInicio) {
        this.RLevantamientoInicio = RLevantamientoInicio;
    }
    public Date getRLevantamientoCierre() {
        return this.RLevantamientoCierre;
    }
    
    public void setRLevantamientoCierre(Date RLevantamientoCierre) {
        this.RLevantamientoCierre = RLevantamientoCierre;
    }
    public Date getRCotizaInicio() {
        return this.RCotizaInicio;
    }
    
    public void setRCotizaInicio(Date RCotizaInicio) {
        this.RCotizaInicio = RCotizaInicio;
    }
    public Date getRCotizaCierre() {
        return this.RCotizaCierre;
    }
    
    public void setRCotizaCierre(Date RCotizaCierre) {
        this.RCotizaCierre = RCotizaCierre;
    }
    public Date getAutorizaCliente() {
        return this.autorizaCliente;
    }
    
    public void setAutorizaCliente(Date autorizaCliente) {
        this.autorizaCliente = autorizaCliente;
    }
    public Date getRAutorizacionInterna() {
        return this.RAutorizacionInterna;
    }
    
    public void setRAutorizacionInterna(Date RAutorizacionInterna) {
        this.RAutorizacionInterna = RAutorizacionInterna;
    }
    public Date getRValuacionInicio() {
        return this.RValuacionInicio;
    }
    
    public void setRValuacionInicio(Date RValuacionInicio) {
        this.RValuacionInicio = RValuacionInicio;
    }
    public Date getEnvioCompania() {
        return this.envioCompania;
    }
    
    public void setEnvioCompania(Date envioCompania) {
        this.envioCompania = envioCompania;
    }
    public Date getAutorizaCompania() {
        return this.autorizaCompania;
    }
    
    public void setAutorizaCompania(Date autorizaCompania) {
        this.autorizaCompania = autorizaCompania;
    }
    public Date getRValuacionCierre() {
        return this.RValuacionCierre;
    }
    
    public void setRValuacionCierre(Date RValuacionCierre) {
        this.RValuacionCierre = RValuacionCierre;
    }
    public Date getMetaValuacion() {
        return this.metaValuacion;
    }
    
    public void setMetaValuacion(Date metaValuacion) {
        this.metaValuacion = metaValuacion;
    }
    public Date getInicioRefacciones() {
        return this.inicioRefacciones;
    }
    
    public void setInicioRefacciones(Date inicioRefacciones) {
        this.inicioRefacciones = inicioRefacciones;
    }
    public Date getCierreRefacciones() {
        return this.cierreRefacciones;
    }
    
    public void setCierreRefacciones(Date cierreRefacciones) {
        this.cierreRefacciones = cierreRefacciones;
    }
    public Date getMetaRefacciones() {
        return this.metaRefacciones;
    }
    
    public void setMetaRefacciones(Date metaRefacciones) {
        this.metaRefacciones = metaRefacciones;
    }
    public Date getRReparacionInicio() {
        return this.RReparacionInicio;
    }
    
    public void setRReparacionInicio(Date RReparacionInicio) {
        this.RReparacionInicio = RReparacionInicio;
    }
    public Date getRHojalateriaFecha() {
        return this.RHojalateriaFecha;
    }
    
    public void setRHojalateriaFecha(Date RHojalateriaFecha) {
        this.RHojalateriaFecha = RHojalateriaFecha;
    }
    public Date getRMecanicaFecha() {
        return this.RMecanicaFecha;
    }
    
    public void setRMecanicaFecha(Date RMecanicaFecha) {
        this.RMecanicaFecha = RMecanicaFecha;
    }
    public Date getRSuspensionFecha() {
        return this.RSuspensionFecha;
    }
    
    public void setRSuspensionFecha(Date RSuspensionFecha) {
        this.RSuspensionFecha = RSuspensionFecha;
    }
    public Date getRElectricoFecha() {
        return this.RElectricoFecha;
    }
    
    public void setRElectricoFecha(Date RElectricoFecha) {
        this.RElectricoFecha = RElectricoFecha;
    }
    public Date getRReparacionCierre() {
        return this.RReparacionCierre;
    }
    
    public void setRReparacionCierre(Date RReparacionCierre) {
        this.RReparacionCierre = RReparacionCierre;
    }
    public Date getMetaReparacion() {
        return this.metaReparacion;
    }
    
    public void setMetaReparacion(Date metaReparacion) {
        this.metaReparacion = metaReparacion;
    }
    public Date getREntregarFecha() {
        return this.REntregarFecha;
    }
    
    public void setREntregarFecha(Date REntregarFecha) {
        this.REntregarFecha = REntregarFecha;
    }
    public Integer getNoFactura() {
        return this.noFactura;
    }
    
    public void setNoFactura(Integer noFactura) {
        this.noFactura = noFactura;
    }
    public Date getFehaFectura() {
        return this.fehaFectura;
    }
    
    public void setFehaFectura(Date fehaFectura) {
        this.fehaFectura = fehaFectura;
    }
    public Date getFechaCierre() {
        return this.fechaCierre;
    }
    
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    public Integer getIdObservacion() {
        return this.idObservacion;
    }
    
    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }
    public String getSession() {
        return this.session;
    }
    
    public void setSession(String session) {
        this.session = session;
    }
    public String getVentana() {
        return this.ventana;
    }
    
    public void setVentana(String ventana) {
        this.ventana = ventana;
    }
    public Double getRefPresupuesto() {
        return this.refPresupuesto;
    }
    
    public void setRefPresupuesto(Double refPresupuesto) {
        this.refPresupuesto = refPresupuesto;
    }
    public Double getRefAutorizadas() {
        return this.refAutorizadas;
    }
    
    public void setRefAutorizadas(Double refAutorizadas) {
        this.refAutorizadas = refAutorizadas;
    }
    public Double getMoPresupuestada() {
        return this.moPresupuestada;
    }
    
    public void setMoPresupuestada(Double moPresupuestada) {
        this.moPresupuestada = moPresupuestada;
    }
    public Double getMoDirecta() {
        return this.moDirecta;
    }
    
    public void setMoDirecta(Double moDirecta) {
        this.moDirecta = moDirecta;
    }
    public Boolean getAutorizadoFacturar() {
        return this.autorizadoFacturar;
    }
    
    public void setAutorizadoFacturar(Boolean autorizadoFacturar) {
        this.autorizadoFacturar = autorizadoFacturar;
    }
    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    public String getPropietario() {
        return this.propietario;
    }
    
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    public String getCodigoAsegurado() {
        return this.codigoAsegurado;
    }
    
    public void setCodigoAsegurado(String codigoAsegurado) {
        this.codigoAsegurado = codigoAsegurado;
    }
    public String getTipoVehiculo() {
        return this.tipoVehiculo;
    }
    
    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
    public String getContratante() {
        return this.contratante;
    }
    
    public void setContratante(String contratante) {
        this.contratante = contratante;
    }
    
    public String getKm() {
        return this.Km;
    }
    
    public Double getVales() {
        return this.vales;
    }
    
    public void setVales(Double vales) {
        this.vales = vales;
    }
    
    public void setKm(String Km) {
        this.Km = Km;
    }
    
    public Set getArchivos() {
        return this.archivos;
    }
    
    public void setArchivos(Set archivos) {
        this.archivos = archivos;
    }
    public Set getPartidasForEnlazada() {
        return this.partidasForEnlazada;
    }
    
    public void setPartidasForEnlazada(Set partidasForEnlazada) {
        this.partidasForEnlazada = partidasForEnlazada;
    }
    public Set getNotas() {
        return this.notas;
    }
    
    public void setNotas(Set notas) {
        this.notas = notas;
    }
    public Set getAdicionaleses() {
        return this.adicionaleses;
    }
    
    public void setAdicionaleses(Set adicionaleses) {
        this.adicionaleses = adicionaleses;
    }
    public Set getAgendas() {
        return this.agendas;
    }
    
    public void setAgendas(Set agendas) {
        this.agendas = agendas;
    }
    public Set getDestajos() {
        return this.destajos;
    }
    
    public void setDestajos(Set destajos) {
        this.destajos = destajos;
    }
    public Set getPagoAdicional() {
        return this.pagoAdicional;
    }
    
    public void setPagoAdicional(Set pagoAdicional) {
        this.pagoAdicional = pagoAdicional;
    }
    public Set getInventarios() {
        return this.inventarios;
    }
    
    public void setInventarios(Set inventarios) {
        this.inventarios = inventarios;
    }
    public Set getFacturas() {
        return this.facturas;
    }
    
    public void setFacturas(Set facturas) {
        this.facturas = facturas;
    }
    public Set getFotos() {
        return this.fotos;
    }
    
    public void setFotos(Set fotos) {
        this.fotos = fotos;
    }
    public Set getMensajes() {
        return this.mensajes;
    }
    
    public void setMensajes(Set mensajes) {
        this.mensajes = mensajes;
    }
    public Set getPartidasForIdOrden() {
        return this.partidasForIdOrden;
    }
    
    public void setPartidasForIdOrden(Set partidasForIdOrden) {
        this.partidasForIdOrden = partidasForIdOrden;
    }
    
    public Set getPedidos() {
        return this.pedidos;
    }
    
    public void setPedidos(Set pedidos) {
        this.pedidos = pedidos;
    }
    
    public boolean eliminaMensaje(Mensaje men) {
        return this.mensajes.remove(men);
    }

    public boolean eliminaFoto(Foto fotos) {
        return this.fotos.remove(fotos);
    }

    public void addFoto(Foto fotos) {
        this.fotos.add(fotos);
    }



    public void addArchivo(Archivo arch) {
        this.archivos.add(arch);
    }
    
    public boolean eliminaArchivo(Archivo arch) {
        return this.archivos.remove(arch);
    }

    public boolean eliminaPartidaOrden(Partida par) {
        return this.partidasForIdOrden.remove(par);
    }

    public boolean eliminaAgenda(Agenda arch) {
        return this.agendas.remove(arch);
    }

    public void addAgenda(Agenda evento) {
        this.agendas.add(evento);
    }

    public boolean eliminaPartidaenlazada(Partida par) {
        return this.partidasForEnlazada.remove(par);
    }

    public void setRPinturaFecha(Date RPinturaFecha) {
        this.RPinturaFecha = RPinturaFecha;
    }
    public Date getRPinturaFecha() {
        return this.RPinturaFecha;
    }

    public Empleado getEmpleadoByRPintura() {
        return this.empleadoByRPintura;
    }
    
    public void setEmpleadoByRPintura(Empleado empleadoByRPintura) {
        this.empleadoByRPintura = empleadoByRPintura;
    }

    public Usuario getUsuarioByRPinturaAsigno() {
        return this.usuarioByRPinturaAsigno;
    }
    
    public void setUsuarioByRPinturaAsigno(Usuario usuarioByRPinturaAsigno) {
        this.usuarioByRPinturaAsigno = usuarioByRPinturaAsigno;
    }
    
    public Double getImporteHojalateria()
    {
        return this.importeHojalateria;
    }
    public void setImporteHojalateria(Double importeHojalateria)
    {
        this.importeHojalateria=importeHojalateria;
    }
    
    public Double getImporteMecanica()
    {
        return this.importeMecanica;
    }
    public void setImporteMecanica(Double importeMecanica)
    {
        this.importeMecanica=importeMecanica;
    }
    public Double getImporteSuspension()
    {
        return this.importeSuspension;
    }
    public void setImporteSuspension(Double importeSuspension)
    {
        this.importeSuspension=importeSuspension;
    }
    
    public Double getImporteElectrico()
    {
        return this.importeElectrico;
    }
    public void setImporteElectrico(Double importeElectrico)
    {
        this.importeElectrico=importeElectrico;
    }
    
    public Double getImportePintura()
    {
        return this.importePintura;
    }
    public void setImportePintura(Double importePintura)
    {
        this.importePintura=importePintura;
    }
    
    public Date getHojalateriaLimite()
    {
        return this.hojalateriaLimite;
    }
    public void setHojalateriaLimite(Date hojalateriaLimite)
    {
        this.hojalateriaLimite=hojalateriaLimite;
    }
    
    public Date getMecanicaLimite()
    {
        return this.mecanicaLimite;
    }
    public void setMecanicaLimite(Date mecanicaLimite)
    {
        this.mecanicaLimite=mecanicaLimite;
    }
    
    public Date getSuspensionLimite()
    {
        return this.suspensionLimite;
    }
    public void setSuspensionLimite(Date suspensionLimite)
    {
        this.suspensionLimite=suspensionLimite;
    }
    
    public Date getElectricoLimite()
    {
        return this.electricoLimite;
    }
    public void setElectricoLimite(Date electricoLimite)
    {
        this.electricoLimite=electricoLimite;
    }
    
    public Date getPinturaLimite()
    {
        return this.pinturaLimite;
    }
    public void setPinturaLimite(Date pinturaLimite)
    {
        this.pinturaLimite=pinturaLimite;
    }
       
}


