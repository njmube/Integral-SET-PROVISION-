package Contabilidad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import mysuite.TComprobanteEx;
import mysuite.TFactDocMX;
import mysuite.TFactDocMX.Cancelaciones;
import mysuite.TFactDocMX.Conceptos;
import mysuite.TFactDocMX.Emisor;
import mysuite.TFactDocMX.Identificacion;
import mysuite.TFactDocMX.Origen;
import mysuite.TFactDocMX.Receptor;
import mysuite.TFactDocMX.Totales;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salvador
 */
public class javaToXML 
{
    private Document xml=new Document();
    public String error="";
    private Element root;
    private Element informacionaduanera;
    private Element compro;
    private String PREFIX="fx", PREFIX_URI="http://www.fact.com.mx/schema/fx";
    private String COMPROBANTE_SCHEMA_XSD= "http://www.fact.com.mx/schema/fx http://www.mysuitemex.com/fact/schema/fx_2010_d.xsd";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:Ss");
    
    
    public void generaRaiz(TFactDocMX datos)
    {
        root = new Element("FactDocMX", PREFIX, PREFIX_URI);
        Namespace namespace=Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("schemaLocation", COMPROBANTE_SCHEMA_XSD, namespace);
        root.addNamespaceDeclaration(namespace);
            root.addContent(new Element("Version", PREFIX, PREFIX_URI).setText(datos.getVersion().toString()));
            generaIdentificacion(datos.getIdentificacion());
            generarCancelaciones(datos.getCancelaciones());
            //Asignacion (Solo para algunas implementaciones in-house con cfdv2.)
            generarOrigen(datos.getOrigen());
            //generarProcesamiento(datos.getProcesamiento());
            generarEmisor(datos.getEmisor());
            generarReceptor(datos.getReceptor());
            generarConceptos(datos.getConceptos());
            generarTotales(datos.getTotales());
            //Complementos
                //Divisas
                //Donatarias
                //Leyendas LeyendasFiscales
                //Nomina
                //PagoEnEspecie
                //Aerolineas
                //ValesDeDespensa 
                //ConsumoDeCombustibles
                //EstadoDeCuentaCombustible
                //NotariosPublicos
                //VehiculoUsado
            generaComprobanteEx(datos.getComprobanteEx());
        getXml().setRootElement(root);
    }

    /**
     * Genera el Nodo de Notificacion del XML y lo agrega a el nodo root
     * @param identif Información de Itenfificacion contenida en la clase TFactDocMX
     */
    public void generaIdentificacion(Identificacion identif)
    {
        Element identificacion = new Element("Identificacion", PREFIX, PREFIX_URI);
            identificacion.addContent(new Element("CdgPaisEmisor", PREFIX, PREFIX_URI).setText(identif.getCdgPaisEmisor().value()));
            identificacion.addContent(new Element("TipoDeComprobante", PREFIX, PREFIX_URI).setText(identif.getTipoDeComprobante().value()));
            identificacion.addContent(new Element("RFCEmisor", PREFIX, PREFIX_URI).setText(identif.getRFCEmisor()));
            identificacion.addContent(new Element("RazonSocialEmisor", PREFIX, PREFIX_URI).setText(identif.getRazonSocialEmisor()));
            identificacion.addContent(new Element("Usuario", PREFIX, PREFIX_URI).setText(identif.getUsuario()));
            if(identif.getNumeroInterno()!=null)
                identificacion.addContent(new Element("NumeroInterno", PREFIX, PREFIX_URI).setText(identif.getNumeroInterno()));
            if(identif.getAsignacionSolicitada()!=null)
            {
                Element AsignacionSolicitada = new Element("AsignacionSolicitada", PREFIX, PREFIX_URI);
                    if(identif.getAsignacionSolicitada().getSerie().compareTo("")!=0)
                        AsignacionSolicitada.addContent(new Element("Serie", PREFIX, PREFIX_URI).setText(identif.getAsignacionSolicitada().getSerie()));
                    if(identif.getAsignacionSolicitada().getFolio().compareTo("")!=0)
                        AsignacionSolicitada.addContent(new Element("Folio", PREFIX, PREFIX_URI).setText(identif.getAsignacionSolicitada().getFolio()));  
                    if(identif.getAsignacionSolicitada().getTiempoDeEmision()!=null)
                        AsignacionSolicitada.addContent(new Element("TiempoDeEmision", PREFIX, PREFIX_URI).setText(sdf.format(identif.getAsignacionSolicitada().getTiempoDeEmision().toGregorianCalendar().getTime())));  
                identificacion.addContent(AsignacionSolicitada);
            }
            identificacion.addContent(new Element("LugarExpedicion", PREFIX, PREFIX_URI).setText(identif.getLugarExpedicion()));
            if(identif.getNumCtaPago()!=null)
                identificacion.addContent(new Element("NumCtaPago", PREFIX, PREFIX_URI).setText(identif.getLugarExpedicion()));
            if(identif.getTotalDeParcialidades()!=null)
            {
                if(identif.getTotalDeParcialidades().getFolioFiscalOrigUuid()!=null)
                    identificacion.addContent(new Element("FolioFiscalOrigUuid", PREFIX, PREFIX_URI).setText(identif.getTotalDeParcialidades().getFolioFiscalOrigUuid()));
                else
                    identificacion.addContent(new Element("FolioFiscalOrigNum", PREFIX, PREFIX_URI).setText(identif.getTotalDeParcialidades().getFolioFiscalOrigNum().toString()));
                if(identif.getTotalDeParcialidades().getSerieFolioFiscalOrig()!=null)
                    identificacion.addContent(new Element("SerieFolioFiscalOrig", PREFIX, PREFIX_URI).setText(identif.getTotalDeParcialidades().getFolioFiscalOrigNum().toString()));
                identificacion.addContent(new Element("FechaFolioFiscalOrig", PREFIX, PREFIX_URI).setText(this.sdf.format(identif.getTotalDeParcialidades().getFechaFolioFiscalOrig().toGregorianCalendar().getTime())));
                identificacion.addContent(new Element("MontoFolioFiscalOrig", PREFIX, PREFIX_URI).setText(identif.getTotalDeParcialidades().getMontoFolioFiscalOrig().getValue().toString()));
            }
            //CadenaOriginal
        root.addContent(identificacion);
    }
    
    /**
     * Genera el Nodo de Cancelaciones del XML y lo agrega a el nodo root
     * @param cancel Información de Cancelacioens contenida en la clase TFactDocMX
     */
    public void generarCancelaciones(Cancelaciones cancel)
    {
        if(cancel!=null)
        {
            Element cancelaciones = new Element("Cancelaciones", PREFIX, PREFIX_URI);
                for(int x=0; x<cancel.getCancelaYSustituye().size(); x++)
                {
                    Element CancelaHijos = new Element("CancelaYSustituye", PREFIX, PREFIX_URI);
                        CancelaHijos.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(cancel.getCancelaYSustituye().get(x).getNumero()));
                        if(cancel.getCancelaYSustituye().get(x).getFecha()!=null)
                            CancelaHijos.addContent(new Element("Fecha", PREFIX, PREFIX_URI).setText(this.sdf.format(cancel.getCancelaYSustituye().get(x).getFecha().toGregorianCalendar().getTime())));
                        if(cancel.getCancelaYSustituye().get(x).getTipoCancelacion()!=null)
                            CancelaHijos.addContent(new Element("TipoCancelacion", PREFIX, PREFIX_URI).setText(cancel.getCancelaYSustituye().get(x).getTipoCancelacion()));
                        if(cancel.getCancelaYSustituye().get(x).getConceptoCancelacion()!=null)
                            CancelaHijos.addContent(new Element("ConceptoCancelacion", PREFIX, PREFIX_URI).setText(cancel.getCancelaYSustituye().get(x).getConceptoCancelacion()));
                    cancelaciones.addContent(CancelaHijos);
                }
            root.addContent(cancelaciones);
        }
    }
    
    /**
     * Genera el Nodo de Origen del XML y lo agrega a el nodo root
     * @param ori Información de Origen contenida en la clase TFactDocMX
     */
    public void generarOrigen(Origen ori)
    {
        if(ori!=null)
        {
            Element origen = new Element("Origen" , PREFIX, PREFIX_URI);
            if(ori.getArea()!=null)
                origen.addContent(new Element("Area", PREFIX, PREFIX_URI).setText(ori.getArea()));
            if(ori.getModulo()!=null)
                origen.addContent(new Element("Modulo", PREFIX, PREFIX_URI).setText(ori.getModulo()));
            root.addContent(origen);
        }
    }
    
    /**
     * Genera el Nodo de Procesamiento del XML y lo agrega a el nodo root
     * @param directorios Información de Directorios contenida en la clase TFactDocMX
     */
    public void generarProcesamiento(mysuite.TDictionaries directorios)
    {
        Element procesamiento = new Element("Procesamiento" , PREFIX, PREFIX_URI);
        for(int a=0; a<directorios.getDictionary().size(); a++)
        {
            Element dis = new Element("Dictionary" , PREFIX, PREFIX_URI);
            dis.setAttribute("name", directorios.getDictionary().get(a).getName());
                for(int x=0; x<directorios.getDictionary().size(); x++)
                {
                    Element entrada = new Element("Entry" , PREFIX, PREFIX_URI);
                    entrada.setAttribute("k", directorios.getDictionary().get(x).getEntry().get(0).getK());
                    entrada.setAttribute("v", directorios.getDictionary().get(x).getEntry().get(1).getK());
                    dis.addContent(entrada);
                }
            procesamiento.addContent(dis);
        }
        root.addContent(procesamiento);
    }
    
    /**
     * Genera el Nodo de Emisor del XML y lo agrega a el nodo root
     * @param emisorFactura Información de Emisor contenida en la clase TFactDocMX
     */
    public void generarEmisor(Emisor emisorFactura)
    {
        Element emisor = new Element("Emisor", PREFIX, PREFIX_URI);
        if(emisorFactura.getDomicilioFiscal()!=null)
        {
            Element emisor_domicilio_fiscal = new Element("DomicilioFiscal", PREFIX, PREFIX_URI);
                emisor_domicilio_fiscal.addContent(new Element("Calle", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getCalle()));
                if(emisorFactura.getDomicilioFiscal().getNumeroExterior()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("NumeroExterior", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getNumeroExterior()));
                if(emisorFactura.getDomicilioFiscal().getNumeroInterior()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("NumeroInterior", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getNumeroInterior()));
                if(emisorFactura.getDomicilioFiscal().getLocalidad()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("Localidad", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getLocalidad()));
                if(emisorFactura.getDomicilioFiscal().getReferencia()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("Referencia", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getReferencia()));
                if(emisorFactura.getDomicilioFiscal().getColonia()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("Colonia", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getColonia()));
                emisor_domicilio_fiscal.addContent(new Element("Municipio", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getMunicipio()));
                emisor_domicilio_fiscal.addContent(new Element("Estado", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getEstado()));
                emisor_domicilio_fiscal.addContent(new Element("Pais", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getPais()));
                emisor_domicilio_fiscal.addContent(new Element("CodigoPostal", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getCodigoPostal()));
                if(emisorFactura.getDomicilioFiscal().getNomContacto()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("NomContacto", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getNomContacto()));
                if(emisorFactura.getDomicilioFiscal().getTelContacto()!=null)
                    emisor_domicilio_fiscal.addContent(new Element("TelContacto", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioFiscal().getTelContacto()));
            emisor.addContent(emisor_domicilio_fiscal);
        }
        if(emisorFactura.getDomicilioDeEmision()!=null)
        {
            Element emisor_domicilio_Emision = new Element("DomicilioDeEmision", PREFIX, PREFIX_URI);
                emisor_domicilio_Emision.addContent(new Element("Calle", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getCalle()));
                if(emisorFactura.getDomicilioDeEmision().getNumeroExterior()!=null)
                    emisor_domicilio_Emision.addContent(new Element("NumeroExterior", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getNumeroExterior()));
                if(emisorFactura.getDomicilioDeEmision().getNumeroInterior()!=null)
                    emisor_domicilio_Emision.addContent(new Element("NumeroInterior", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getNumeroInterior()));
                if(emisorFactura.getDomicilioDeEmision().getLocalidad()!=null)
                    emisor_domicilio_Emision.addContent(new Element("Localidad", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getLocalidad()));
                if(emisorFactura.getDomicilioDeEmision().getReferencia()!=null)
                    emisor_domicilio_Emision.addContent(new Element("Referencia", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getReferencia()));
                if(emisorFactura.getDomicilioDeEmision().getColonia()!=null)
                    emisor_domicilio_Emision.addContent(new Element("Colonia", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getColonia()));
                emisor_domicilio_Emision.addContent(new Element("Municipio", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getMunicipio()));
                emisor_domicilio_Emision.addContent(new Element("Estado", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getEstado()));
                emisor_domicilio_Emision.addContent(new Element("Pais", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getPais()));
                emisor_domicilio_Emision.addContent(new Element("CodigoPostal", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getCodigoPostal()));
                if(emisorFactura.getDomicilioDeEmision().getNomContacto()!=null)
                    emisor_domicilio_Emision.addContent(new Element("NomContacto", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getNomContacto()));
                if(emisorFactura.getDomicilioDeEmision().getTelContacto()!=null)
                    emisor_domicilio_Emision.addContent(new Element("TelContacto", PREFIX, PREFIX_URI).setText(emisorFactura.getDomicilioDeEmision().getTelContacto()));
            emisor.addContent(emisor_domicilio_Emision);
        }
            Element regimenfiscal = new Element("RegimenFiscal", PREFIX, PREFIX_URI);
                for(int x=0; x<emisorFactura.getRegimenFiscal().getRegimen().size(); x++)
                    regimenfiscal.addContent(new Element("Regimen", PREFIX, PREFIX_URI).setText(emisorFactura.getRegimenFiscal().getRegimen().get(x)));
            emisor.addContent(regimenfiscal);
        root.addContent(emisor);
    }
    
    /**
     * Genera el Nodo de Receptor del XML y lo agrega a el nodo root
     * @param recep Información de Receptor contenida en la clase TFactDocMX
     */
    public void generarReceptor(Receptor recep)
    {
        Element receptor = new Element("Receptor", PREFIX, PREFIX_URI);
            receptor.addContent(new Element("CdgPaisReceptor", PREFIX, PREFIX_URI).setText(recep.getCdgPaisReceptor().value()));
            receptor.addContent(new Element("RFCReceptor", PREFIX, PREFIX_URI).setText(recep.getRFCReceptor()));
            if(recep.getTaxID()!=null)
                receptor.addContent(new Element("TaxID", PREFIX, PREFIX_URI).setText(recep.getTaxID()));
            if(recep.getNombreReceptor()!=null)
                receptor.addContent(new Element("NombreReceptor", PREFIX, PREFIX_URI).setText(recep.getNombreReceptor()));
            if(recep.getDomicilio()!=null)
            {
                Element domicilio=new Element("Domicilio", PREFIX, PREFIX_URI);
                    if(recep.getDomicilio().getDomicilioFiscalMexicano()!=null)
                    {
                        Element Domiciliofiscalmexicano=new Element("DomicilioFiscalMexicano", PREFIX, PREFIX_URI);
                            Domiciliofiscalmexicano.addContent(new Element("Calle", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getCalle()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getNumeroExterior()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("NumeroExterior", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getNumeroExterior()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getNumeroInterior()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("NumeroInterior", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getNumeroInterior()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getLocalidad()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("Localidad", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getLocalidad()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getReferencia()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("Referencia", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getReferencia()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getColonia()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("Colonia", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getColonia()));
                            Domiciliofiscalmexicano.addContent(new Element("Municipio", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getMunicipio()));
                            Domiciliofiscalmexicano.addContent(new Element("Estado", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getEstado()));
                            Domiciliofiscalmexicano.addContent(new Element("Pais", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getPais()));
                            Domiciliofiscalmexicano.addContent(new Element("CodigoPostal", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getCodigoPostal()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getNomContacto()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("NomContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getNomContacto()));
                            if(recep.getDomicilio().getDomicilioFiscalMexicano().getTelContacto()!=null)
                                Domiciliofiscalmexicano.addContent(new Element("TelContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getDomicilioFiscalMexicano().getTelContacto()));
                        domicilio.addContent(Domiciliofiscalmexicano);
                    }
                    else
                    {
                        Element otrodomicilio=new Element("OtroDomicilio", PREFIX, PREFIX_URI);
                            if(recep.getDomicilio().getOtroDomicilio().getCalle()!=null)
                                otrodomicilio.addContent(new Element("Calle", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getCalle()));
                            if(recep.getDomicilio().getOtroDomicilio().getNumeroExterior()!=null)
                                otrodomicilio.addContent(new Element("NumeroExterior", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getNumeroExterior()));
                            if(recep.getDomicilio().getOtroDomicilio().getNumeroInterior()!=null)
                                otrodomicilio.addContent(new Element("NumeroInterior", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getNumeroInterior()));
                            if(recep.getDomicilio().getOtroDomicilio().getLocalidad()!=null)
                                otrodomicilio.addContent(new Element("Localidad", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getLocalidad()));
                            if(recep.getDomicilio().getOtroDomicilio().getReferencia()!=null)
                                otrodomicilio.addContent(new Element("Referencia", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getReferencia()));
                            if(recep.getDomicilio().getOtroDomicilio().getColonia()!=null)
                                otrodomicilio.addContent(new Element("Colonia", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getColonia()));
                            if(recep.getDomicilio().getOtroDomicilio().getMunicipio()!=null)
                                otrodomicilio.addContent(new Element("Municipio", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getMunicipio()));
                            if(recep.getDomicilio().getOtroDomicilio().getEstado()!=null)
                                otrodomicilio.addContent(new Element("Estado", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getEstado()));
                            otrodomicilio.addContent(new Element("Pais").setText(recep.getDomicilio().getOtroDomicilio().getPais()));
                            if(recep.getDomicilio().getOtroDomicilio().getCodigoPostal()!=null)
                                otrodomicilio.addContent(new Element("CodigoPostal", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getCodigoPostal()));
                            if(recep.getDomicilio().getOtroDomicilio().getNomContacto()!=null)
                                otrodomicilio.addContent(new Element("NomContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getNomContacto()));
                            if(recep.getDomicilio().getOtroDomicilio().getTelContacto()!=null)
                                otrodomicilio.addContent(new Element("TelContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilio().getOtroDomicilio().getTelContacto()));
                        domicilio.addContent(otrodomicilio);
                    }
                receptor.addContent(domicilio);
            }

            if(recep.getDomicilioDeRecepcion()!=null)
            {
                Element domiciliorecepcion=new Element("DomicilioDeRecepcion");
                    if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano()!=null)
                    {
                        Element Domiciliofiscalmexicano1=new Element("DomicilioFiscalMexicano", PREFIX, PREFIX_URI);
                            Domiciliofiscalmexicano1.addContent(new Element("Calle", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getCalle()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getNumeroExterior()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("NumeroExterior", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getNumeroExterior()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getNumeroInterior()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("NumeroInterior", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getNumeroInterior()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getLocalidad()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("Localidad", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getLocalidad()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getReferencia()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("Referencia", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getReferencia()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getColonia()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("Colonia", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getColonia()));
                            Domiciliofiscalmexicano1.addContent(new Element("Municipio", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getMunicipio()));
                            Domiciliofiscalmexicano1.addContent(new Element("Estado", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getEstado()));
                            Domiciliofiscalmexicano1.addContent(new Element("Pais", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getPais()));
                            Domiciliofiscalmexicano1.addContent(new Element("CodigoPostal", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getCodigoPostal()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getNomContacto()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("NomContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getNomContacto()));
                            if(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getTelContacto()!=null)
                                Domiciliofiscalmexicano1.addContent(new Element("TelContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getDomicilioFiscalMexicano().getTelContacto()));
                        domiciliorecepcion.addContent(Domiciliofiscalmexicano1);
                    }
                    else
                    {
                        Element otrodomicilio1=new Element("OtroDomicilio", PREFIX, PREFIX_URI);
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getCalle()!=null)
                                otrodomicilio1.addContent(new Element("Calle", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getCalle()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getNumeroExterior()!=null)
                                otrodomicilio1.addContent(new Element("NumeroExterior", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getNumeroExterior()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getNumeroInterior()!=null)
                                otrodomicilio1.addContent(new Element("NumeroInterior", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getNumeroInterior()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getLocalidad()!=null)
                                otrodomicilio1.addContent(new Element("Localidad", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getLocalidad()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getReferencia()!=null)
                                otrodomicilio1.addContent(new Element("Referencia", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getReferencia()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getColonia()!=null)
                                otrodomicilio1.addContent(new Element("Colonia", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getColonia()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getMunicipio()!=null)
                                otrodomicilio1.addContent(new Element("Municipio", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getMunicipio()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getEstado()!=null)
                                otrodomicilio1.addContent(new Element("Estado", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getEstado()));
                            otrodomicilio1.addContent(new Element("Pais", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getPais()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getCodigoPostal()!=null)
                                otrodomicilio1.addContent(new Element("CodigoPostal", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getCodigoPostal()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getNomContacto()!=null)
                                otrodomicilio1.addContent(new Element("NomContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getNomContacto()));
                            if(recep.getDomicilioDeRecepcion().getOtroDomicilio().getTelContacto()!=null)
                                otrodomicilio1.addContent(new Element("TelContacto", PREFIX, PREFIX_URI).setText(recep.getDomicilioDeRecepcion().getOtroDomicilio().getTelContacto()));
                        domiciliorecepcion.addContent(otrodomicilio1);
                    }
                receptor.addContent(domiciliorecepcion);
            }
        root.addContent(receptor);
    }
    
    /**
     * Genera el Nodo de Conceptos del XML y lo agrega a el nodo root
     * @param concep Información de Conceptos contenida en la clase TFactDocMX
     */
    public void generarConceptos(Conceptos concep)
    {
        Element conceptos = new Element("Conceptos", PREFIX, PREFIX_URI);
            for(int x=0; x<concep.getConcepto().size(); x++)
            {
                Element concepto = new Element("Concepto", PREFIX, PREFIX_URI);
                    concepto.addContent(new Element("Cantidad", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getCantidad().toString()));
                    concepto.addContent(new Element("UnidadDeMedida", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getUnidadDeMedida()));
                    if(concep.getConcepto().get(x).getCodigo()!=null)
                        concepto.addContent(new Element("Codigo", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getCodigo()));
                    concepto.addContent(new Element("Descripcion", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getDescripcion()));
                    concepto.addContent(new Element("ValorUnitario", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getValorUnitario().getValue().toString()));
                    concepto.addContent(new Element("Importe", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getImporte().getValue().toString()));
                    if(concep.getConcepto().get(x).getOpciones()!=null)
                    {
                        Element opciones = new Element("Opciones", PREFIX, PREFIX_URI);
                            if(concep.getConcepto().get(x).getOpciones().getDatosDeImportacion()!=null)
                            {
                                Element datosdeimportacion= new Element("DatosDeImportacion", PREFIX, PREFIX_URI);
                                    for(int y=0; y<concep.getConcepto().get(x).getOpciones().getDatosDeImportacion().getInformacionAduanera().size(); y++)
                                    {
                                        informacionaduanera= new Element("InformacionAduanera", PREFIX, PREFIX_URI);
                                            informacionaduanera.addContent(new Element("NumeroDePedimento", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getOpciones().getDatosDeImportacion().getInformacionAduanera().get(y).getNumeroDePedimento()));
                                            informacionaduanera.addContent(new Element("FechaDePedimento", PREFIX, PREFIX_URI).setText(sdf.format(concep.getConcepto().get(x).getOpciones().getDatosDeImportacion().getInformacionAduanera().get(y).getFechaDePedimento().toGregorianCalendar().getTime())));
                                            informacionaduanera.addContent(new Element("NombreDeAduana", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getOpciones().getDatosDeImportacion().getInformacionAduanera().get(y).getNombreDeAduana()));
                                        datosdeimportacion.addContent(informacionaduanera);
                                    }
                                opciones.addContent(datosdeimportacion);
                            }
                            if(concep.getConcepto().get(x).getOpciones().getCuentaPredial()!=null)
                            {
                                opciones.addContent(new Element("CuentaPredial", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getOpciones().getCuentaPredial()));
                            }
                            //ComplementoConcepto
                            
                        concepto.addContent(opciones);
                    }
                    if(concep.getConcepto().get(x).getConceptoEx()!=null)
                    {
                        Element concepto_ext = new Element("ConceptoEx", PREFIX, PREFIX_URI);
                            if(concep.getConcepto().get(x).getConceptoEx().getPrecioCombustibles()!=null)
                                concepto_ext.addContent(new Element("PrecioCombustibles", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getPrecioCombustibles().getValue().toString()));
                            if(concep.getConcepto().get(x).getConceptoEx().getPrecioSugerido()!=null)
                                concepto_ext.addContent(new Element("PrecioSugerido", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getPrecioSugerido().getValue().toString()));
                            if(concep.getConcepto().get(x).getConceptoEx().getPrecioLista()!=null)
                                concepto_ext.addContent(new Element("PrecioLista", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getPrecioLista().getValue().toString()));
                            if(concep.getConcepto().get(x).getConceptoEx().getImporteLista()!=null)
                                concepto_ext.addContent(new Element("ImporteLista", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImporteLista().getValue().toString()));
                            
                            if(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos()!=null)
                            {
                                Element descuentos_o_recargos=new Element("DescuentosYRecargos", PREFIX, PREFIX_URI);
                                for(int a=0; a<concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().size(); a++)
                                {
                                    Element hijoDescuento1=new Element("DescuentoORecargo", PREFIX, PREFIX_URI);
                                        hijoDescuento1.addContent(new Element("Operacion", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getOperacion().value()));
                                        hijoDescuento1.addContent(new Element("Imputacion", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getImputacion().value()));
                                        hijoDescuento1.addContent(new Element("Servicio", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getServicio().value()));
                                        if(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getDescripcion()!=null)
                                        {
                                            hijoDescuento1.addContent(new Element("Descripcion", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getDescripcion()));
                                        }
                                        hijoDescuento1.addContent(new Element("Base", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getBase().getValue().toString()));
                                        hijoDescuento1.addContent(new Element("Tasa", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getTasa().toString()));
                                        hijoDescuento1.addContent(new Element("Monto", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getDescuentosYRecargos().getDescuentoORecargo().get(a).getMonto().getValue().toString()));
                                    descuentos_o_recargos.addContent(hijoDescuento1);
                                }
                                concepto_ext.addContent(descuentos_o_recargos);
                            }
                            if(concep.getConcepto().get(x).getConceptoEx().getImpuestos()!=null)
                            {
                                Element impuestos=new Element("Impuestos", PREFIX, PREFIX_URI);
                                    for(int im=0; im<concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().size(); im++)
                                    {
                                        Element impuesto=new Element("Impuesto", PREFIX, PREFIX_URI);
                                            impuesto.addContent(new Element("Contexto", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().get(im).getContexto().value()));
                                            impuesto.addContent(new Element("Operacion", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().get(im).getOperacion().value()));
                                            impuesto.addContent(new Element("Codigo", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().get(im).getCodigo()));
                                            impuesto.addContent(new Element("Base", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().get(im).getBase().getValue().toString()));
                                            impuesto.addContent(new Element("Tasa", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().get(im).getTasa().toString()));
                                            impuesto.addContent(new Element("Monto", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImpuestos().getImpuesto().get(im).getMonto().getValue().toString()));
                                        impuestos.addContent(impuesto);
                                    }
                                concepto_ext.addContent(impuestos);
                            }
                            if(concep.getConcepto().get(x).getConceptoEx().getImporteTotal()!=null)
                            {
                                concepto_ext.addContent(new Element("ImporteTotal", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getImporteTotal().getValue().toString()));
                            }
                            if(concep.getConcepto().get(x).getConceptoEx().getCategoria()!=null)
                            {
                                concepto_ext.addContent(new Element("Categoria", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getCategoria()));
                            }
                            if(concep.getConcepto().get(x).getConceptoEx().getMapfre()!=null)
                            {
                                Element map=new Element("Mapfre", PREFIX, PREFIX_URI);
                                    map.addContent(new Element("TipoListaDePrecios", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getMapfre().getTipoListaDePrecios()));
                                    map.addContent(new Element("IndiceListaDePrecios", PREFIX, PREFIX_URI).setText(concep.getConcepto().get(x).getConceptoEx().getMapfre().getIndiceListaDePrecios()));
                                concepto_ext.addContent(map);
                            }
                        concepto.addContent(concepto_ext);
                    }
                conceptos.addContent(concepto);
            }
        root.addContent(conceptos);
    }
    
    /**
     * Genera el Nodo de Totales del XML y lo agrega a el nodo root
     * @param tot Información de Totales contenida en la clase TFactDocMX
     */
    public void generarTotales(Totales tot)
    {
        Element totales=new Element("Totales", PREFIX, PREFIX_URI);
            totales.addContent(new Element("Moneda", PREFIX, PREFIX_URI).setText(tot.getMoneda().value()));
            totales.addContent(new Element("TipoDeCambioVenta", PREFIX, PREFIX_URI).setText(tot.getTipoDeCambioVenta().toString()));
            totales.addContent(new Element("SubTotalBruto", PREFIX, PREFIX_URI).setText(tot.getSubTotalBruto().getValue().toString()));
            totales.addContent(new Element("SubTotal", PREFIX, PREFIX_URI).setText(tot.getSubTotal().getValue().toString()));
            if(tot.getDescuentosYRecargos()!=null)
            {
                if(tot.getDescuentosYRecargos().getDescuentoORecargo().size()>0)
                {
                Element descuentosrecargos=new Element("DescuentosYRecargos", PREFIX, PREFIX_URI);
                    for(int a=0; a<tot.getDescuentosYRecargos().getDescuentoORecargo().size(); a++)
                    {
                        Element hijoDescuento=new Element("DescuentoORecargo", PREFIX, PREFIX_URI);
                            hijoDescuento.addContent(new Element("Operacion", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getOperacion().value()));
                            hijoDescuento.addContent(new Element("Imputacion", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getImputacion().value()));
                            hijoDescuento.addContent(new Element("Servicio", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getServicio().value()));
                            if(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getDescripcion()!=null)
                            {
                                hijoDescuento.addContent(new Element("Descripcion", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getDescripcion()));
                            }
                            hijoDescuento.addContent(new Element("Base", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getBase().getValue().toString()));
                            hijoDescuento.addContent(new Element("Tasa", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getTasa().toString()));
                            hijoDescuento.addContent(new Element("Monto", PREFIX, PREFIX_URI).setText(tot.getDescuentosYRecargos().getDescuentoORecargo().get(a).getMonto().getValue().toString()));
                        descuentosrecargos.addContent(hijoDescuento);
                    }
                totales.addContent(descuentosrecargos);
                }
            }
            Element resumenDeDescuentosRecargos=new Element("ResumenDeDescuentosYRecargos", PREFIX, PREFIX_URI);
                resumenDeDescuentosRecargos.addContent(new Element("TotalDescuentos", PREFIX, PREFIX_URI).setText(tot.getResumenDeDescuentosYRecargos().getTotalDescuentos().getValue().toString()));
                resumenDeDescuentosRecargos.addContent(new Element("TotalRecargos", PREFIX, PREFIX_URI).setText(tot.getResumenDeDescuentosYRecargos().getTotalRecargos().getValue().toString()));
            totales.addContent(resumenDeDescuentosRecargos);
            if(tot.getImpuestos()!=null)
            {
                Element impuestos=new Element("Impuestos", PREFIX, PREFIX_URI);
                    for(int im=0; im<tot.getImpuestos().getImpuesto().size(); im++)
                    {
                        Element impuesto=new Element("Impuesto", PREFIX, PREFIX_URI);
                            impuesto.addContent(new Element("Contexto", PREFIX, PREFIX_URI).setText(tot.getImpuestos().getImpuesto().get(im).getContexto().value()));
                            impuesto.addContent(new Element("Operacion", PREFIX, PREFIX_URI).setText(tot.getImpuestos().getImpuesto().get(im).getOperacion().value()));
                            impuesto.addContent(new Element("Codigo", PREFIX, PREFIX_URI).setText(tot.getImpuestos().getImpuesto().get(im).getCodigo()));
                            impuesto.addContent(new Element("Base", PREFIX, PREFIX_URI).setText(tot.getImpuestos().getImpuesto().get(im).getBase().getValue().toString()));
                            impuesto.addContent(new Element("Tasa", PREFIX, PREFIX_URI).setText(tot.getImpuestos().getImpuesto().get(im).getTasa().toString()));
                            impuesto.addContent(new Element("Monto", PREFIX, PREFIX_URI).setText(tot.getImpuestos().getImpuesto().get(im).getMonto().getValue().toString()));
                        impuestos.addContent(impuesto);
                    }
                totales.addContent(impuestos);
            }
            Element resumenImpuestos=new Element("ResumenDeImpuestos", PREFIX, PREFIX_URI);
                resumenImpuestos.addContent(new Element("TotalTrasladosFederales", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalTrasladosFederales().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalIVATrasladado", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalIVATrasladado().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalIEPSTrasladado", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalIEPSTrasladado().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalRetencionesFederales", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalRetencionesFederales().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalISRRetenido", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalISRRetenido().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalIVARetenido", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalIVARetenido().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalTrasladosLocales", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalTrasladosLocales().getValue().toString()));
                resumenImpuestos.addContent(new Element("TotalRetencionesLocales", PREFIX, PREFIX_URI).setText(tot.getResumenDeImpuestos().getTotalRetencionesLocales().getValue().toString()));
            totales.addContent(resumenImpuestos);
            totales.addContent(new Element("Total", PREFIX, PREFIX_URI).setText(tot.getTotal().getValue().toString()));
            totales.addContent(new Element("TotalEnLetra", PREFIX, PREFIX_URI).setText(tot.getTotalEnLetra()));
            totales.addContent(new Element("FormaDePago", PREFIX, PREFIX_URI).setText(tot.getFormaDePago()));
            //resumen Aduanero
            root.addContent(totales);
    }
    
    public void generaComprobanteEx(TComprobanteEx comprobante)
    {
            compro=new Element("ComprobanteEx", PREFIX, PREFIX_URI);
            if(comprobante.getDatosDeNegocio()!=null)
            {
                Element datos_negocio= new Element("DatosDeNegocio", PREFIX, PREFIX_URI);
                    if(comprobante.getDatosDeNegocio().getDivision()!=null)
                        datos_negocio.addContent(new Element("Division", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getDivision()));
                    if(comprobante.getDatosDeNegocio().getLineaDeNegocio()!=null)
                    datos_negocio.addContent(new Element("LineaDeNegocio", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getLineaDeNegocio()));
                    if(comprobante.getDatosDeNegocio().getRegion()!=null)
                        datos_negocio.addContent(new Element("Region", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getRegion()));
                    if(comprobante.getDatosDeNegocio().getSucursal()!=null)
                        datos_negocio.addContent(new Element("Sucursal", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getSucursal()));
                    if(comprobante.getDatosDeNegocio().getEjecutivo()!=null)
                        datos_negocio.addContent(new Element("Ejecutivo", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getEjecutivo()));
                    if(comprobante.getDatosDeNegocio().getElaboradoPor()!=null)
                        datos_negocio.addContent(new Element("ElaboradoPor", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getElaboradoPor()));
                    if(comprobante.getDatosDeNegocio().getVendedor()!=null)
                        datos_negocio.addContent(new Element("Vendedor", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeNegocio().getVendedor()));
                compro.addContent(datos_negocio);
            }
            if(comprobante.getDatosDeIntercambio()!=null)
            {
                Element sucursal= new Element("DatosDeIntercambio", PREFIX, PREFIX_URI);
                    sucursal.addContent(new Element("SenderId", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeIntercambio().getSenderId()));
                    sucursal.addContent(new Element("ReceiverId", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeIntercambio().getReceiverId()));
                compro.addContent(sucursal);                
            }
            if(comprobante.getDatosComerciales()!=null)
            {
                Element datos_comerciales= new Element("DatosComerciales", PREFIX, PREFIX_URI);
                    if(comprobante.getDatosComerciales().getRelacionComercial()!=null)
                        datos_comerciales.addContent(new Element("RelacionComercial", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getRelacionComercial().value()));
                    else
                    {
                        if(comprobante.getDatosComerciales().getRfcParaAddendaDeTercero()!=null)
                            datos_comerciales.addContent(new Element("RfcParaAddendaDeTercero", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getRfcParaAddendaDeTercero()));
                    }
                    if(comprobante.getDatosComerciales().getNumeroDeProveedor()!=null)
                        datos_comerciales.addContent(new Element("NumeroDeProveedor", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getNumeroDeProveedor()));
                    if(comprobante.getDatosComerciales().getSubAddenda1()!=null)
                        datos_comerciales.addContent(new Element("SubAddenda1", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getSubAddenda1()));
                    if(comprobante.getDatosComerciales().getSubAddenda2()!=null)
                        datos_comerciales.addContent(new Element("SubAddenda2", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getSubAddenda2()));
                    if(comprobante.getDatosComerciales().getOrdenDeCompra()!=null)
                    {
                        Element orden_de_compra=new Element("OrdenDeCompra", PREFIX, PREFIX_URI);
                        if(comprobante.getDatosComerciales().getOrdenDeCompra().getFecha()!=null)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            orden_de_compra.addContent(new Element("Fecha", PREFIX, PREFIX_URI).setText(sdf.format(comprobante.getDatosComerciales().getOrdenDeCompra().getFecha().toGregorianCalendar().getTime())));
                        }
                        for(int n=0; n<comprobante.getDatosComerciales().getOrdenDeCompra().getNumero().size(); n++)
                            if(n<32)
                                orden_de_compra.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getOrdenDeCompra().getNumero().get(n)));
                        if(comprobante.getDatosComerciales().getOrdenDeCompra().getTipo()!=null)
                            orden_de_compra.addContent(new Element("Tipo", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getOrdenDeCompra().getTipo()));
                        datos_comerciales.addContent(orden_de_compra);
                    }
                    if(comprobante.getDatosComerciales().getContrarrecibo()!=null)
                    {
                        Element comtratr_recibo=new Element("Contrarrecibo", PREFIX, PREFIX_URI);
                        if(comprobante.getDatosComerciales().getContrarrecibo().getFecha()!=null)
                            comtratr_recibo.addContent(new Element("Fecha", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getContrarrecibo().getFecha().toXMLFormat()));
                        for(int n=0; n<comprobante.getDatosComerciales().getContrarrecibo().getNumero().size(); n++)
                            if(n<32)
                                comtratr_recibo.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getContrarrecibo().getNumero().get(n)));
                        if(comprobante.getDatosComerciales().getContrarrecibo().getTipo()!=null)
                            comtratr_recibo.addContent(new Element("Tipo", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getContrarrecibo().getTipo()));
                        datos_comerciales.addContent(comtratr_recibo);
                    }
                    if(comprobante.getDatosComerciales().getNumeroDeDepartamento()!=null)
                        datos_comerciales.addContent(new Element("NumeroDeDepartamento", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getNumeroDeDepartamento()));
                    if(comprobante.getDatosComerciales().getNumeroDeCliente()!=null)
                        datos_comerciales.addContent(new Element("NumeroDeCliente", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getNumeroDeCliente()));
                    if(comprobante.getDatosComerciales().getOrdenDeVenta()!=null)
                    {
                        Element orden_de_venta=new Element("OrdenDeVenta", PREFIX, PREFIX_URI);
                        if(comprobante.getDatosComerciales().getOrdenDeVenta().getFecha()!=null)
                            orden_de_venta.addContent(new Element("Fecha", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getOrdenDeVenta().getFecha().toXMLFormat()));
                        for(int n=0; n<comprobante.getDatosComerciales().getOrdenDeVenta().getNumero().size(); n++)
                            if(n<32)
                                orden_de_venta.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getOrdenDeVenta().getNumero().get(n)));
                        if(comprobante.getDatosComerciales().getOrdenDeVenta().getTipo()!=null)
                            orden_de_venta.addContent(new Element("Tipo", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getOrdenDeVenta().getTipo()));
                        datos_comerciales.addContent(orden_de_venta);
                    }
                    if(comprobante.getDatosComerciales().getContrato()!=null)
                    {
                        Element contrato=new Element("Contrato", PREFIX, PREFIX_URI);
                        if(comprobante.getDatosComerciales().getContrato().getFecha()!=null)
                            contrato.addContent(new Element("Fecha", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getContrato().getFecha().toXMLFormat()));
                        for(int n=0; n<comprobante.getDatosComerciales().getContrato().getNumero().size(); n++)
                            if(n<32)
                                contrato.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getContrato().getNumero().get(n)));
                        if(comprobante.getDatosComerciales().getContrato().getTipo()!=null)
                            contrato.addContent(new Element("Tipo", PREFIX, PREFIX_URI).setText(comprobante.getDatosComerciales().getContrato().getTipo()));
                        datos_comerciales.addContent(contrato);
                    }
                compro.addContent(datos_comerciales);
            }   
            if(comprobante.getTerminosDePago()!=null)
            {
                Element terminos_de_pago= new Element("TerminosDePago", PREFIX, PREFIX_URI);//(QUALITAS)
                    if(comprobante.getTerminosDePago().getDiasDePago()!=null)
                        terminos_de_pago.addContent(new Element("DiasDePago", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getDiasDePago().toString()));
                    if(comprobante.getTerminosDePago().getFechaDeVencimiento()!=null)
                        terminos_de_pago.addContent(new Element("FechaDeVencimiento", PREFIX, PREFIX_URI).setText(this.sdf.format(comprobante.getTerminosDePago().getFechaDeVencimiento().toGregorianCalendar().getTime()) ));
                    if(comprobante.getTerminosDePago().getCodigoDeTerminoDePago()!=null)
                        terminos_de_pago.addContent(new Element("FechaDeVencimiento", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getCodigoDeTerminoDePago()));
                    if(comprobante.getTerminosDePago().getCodigoDeTerminoDePago()!=null)
                        terminos_de_pago.addContent(new Element("CodigoDeTerminoDePago", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getCodigoDeTerminoDePago()));
                    terminos_de_pago.addContent(new Element("MetodoDePago", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getMetodoDePago()));
                    if(comprobante.getTerminosDePago().getMedioDePago()!=null)
                        terminos_de_pago.addContent(new Element("MedioDePago", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getMedioDePago()));
                    if(comprobante.getTerminosDePago().getCondicionesDePago()!=null)
                        terminos_de_pago.addContent(new Element("CondicionesDePago", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getCondicionesDePago()));
                    if(comprobante.getTerminosDePago().getPeriodoFacturado()!=null)
                    {
                        Element preriodo_facturado= new Element("PeriodoFacturado", PREFIX, PREFIX_URI);
                            preriodo_facturado.addContent(new Element("Desde", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getPeriodoFacturado().getDesde().toXMLFormat()));
                            preriodo_facturado.addContent(new Element("Hasta", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getPeriodoFacturado().getHasta().toXMLFormat()));
                        terminos_de_pago.addContent(preriodo_facturado);
                    }
                    if(comprobante.getTerminosDePago().getPeriodoDeLiquidacion()!=null)
                    {
                        Element preriodo_de_liquidacion= new Element("PeriodoDeLiquidacion", PREFIX, PREFIX_URI);
                            preriodo_de_liquidacion.addContent(new Element("Desde", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getPeriodoDeLiquidacion().getDesde().toXMLFormat()));
                            preriodo_de_liquidacion.addContent(new Element("Hasta", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getPeriodoDeLiquidacion().getHasta().toXMLFormat()));
                        terminos_de_pago.addContent(preriodo_de_liquidacion);
                    }
                    if(comprobante.getTerminosDePago().getFechaDePago()!=null)
                        terminos_de_pago.addContent(new Element("FechaDePago", PREFIX, PREFIX_URI).setText(comprobante.getTerminosDePago().getFechaDePago().toXMLFormat()));
                compro.addContent(terminos_de_pago);
            }
            /*DescuentosPorProntoPago
            DatosDeEmbarque(QUALITAS)*/
            if(comprobante.getDatosDeEmbarque()!=null)
            {
                //.getCodigo();
                Element datosEnbarque= new Element("DatosDeEmbarque", PREFIX, PREFIX_URI);
                if(comprobante.getDatosDeEmbarque().getLugarDeEntrega()!=null)
                {
                    Element entrega= new Element("LugarDeEntrega", PREFIX, PREFIX_URI);
                    entrega.addContent(new Element("Codigo", PREFIX, PREFIX_URI).setText(comprobante.getDatosDeEmbarque().getLugarDeEntrega().getCodigo()));
                    datosEnbarque.addContent(entrega);
                }
                compro.addContent(datosEnbarque);
            }
            /*DatosAdicionalesDeEmisor(QUALITAS)*/
            if(comprobante.getDatosAdicionalesDeEmisor()!=null)
            {
                Element datosadicionalesEmisor= new Element("DatosAdicionalesDeEmisor", PREFIX, PREFIX_URI);
                if(comprobante.getDatosAdicionalesDeEmisor().getRegistroPublico()!=null)
                    datosadicionalesEmisor.addContent(new Element("RegistroPublico", PREFIX, PREFIX_URI).setText(comprobante.getDatosAdicionalesDeEmisor().getRegistroPublico()));
                compro.addContent(datosadicionalesEmisor);
            }
            /*DatosAdicionalesDeReceptor*/
            if(comprobante.getDatosAdicionalesDeEmisor()!=null)
            {
                Element datosadicionalesReceptor= new Element("DatosAdicionalesDeReceptor", PREFIX, PREFIX_URI);
                if(comprobante.getDatosAdicionalesDeReceptor().getRegistroPublico()!=null)
                    datosadicionalesReceptor.addContent(new Element("RegistroPublico", PREFIX, PREFIX_URI).setText(comprobante.getDatosAdicionalesDeReceptor().getRegistroPublico()));
                compro.addContent(datosadicionalesReceptor);
            }
            /*ReferenciasBancarias(QUALITAS)*/
            if(comprobante.getReferenciasBancarias()!=null)
            {
                Element refBanco= new Element("ReferenciasBancarias", PREFIX, PREFIX_URI);
                for(int pos=0; pos<comprobante.getReferenciasBancarias().getReferenciaBancaria().size(); pos++)
                {
                    Element ref= new Element("ReferenciaBancaria", PREFIX, PREFIX_URI);
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getMoneda()!=null)
                        ref.addContent(new Element("Moneda", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getMoneda().value()));
                    ref.addContent(new Element("Banco", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getBanco()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getSucursal()!=null)
                        ref.addContent(new Element("Sucursal", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getSucursal()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getPlaza()!=null)
                        ref.addContent(new Element("Plaza", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getPlaza()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getTitular()!=null)
                        ref.addContent(new Element("Titular", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getTitular()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getCuenta()!=null)
                        ref.addContent(new Element("Cuenta", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getCuenta()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getCLABE()!=null)
                        ref.addContent(new Element("CLABE", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getCLABE()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getConvenio()!=null)
                        ref.addContent(new Element("Convenio", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getConvenio()));
                    if(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getRefCliente()!=null)
                        ref.addContent(new Element("RefCliente", PREFIX, PREFIX_URI).setText(comprobante.getReferenciasBancarias().getReferenciaBancaria().get(pos).getRefCliente()));
                    refBanco.addContent(ref);
                }
                compro.addContent(refBanco);
            }
            /*DocumentosReferenciados
            ImportesDesglosados
            Peso
            Volumen
            TotalCajas
            TotalPiezas
            Cotizaciones*/
            if(comprobante.getTextosDeCabecera()!=null)
            {
                Element cabecera= new Element("TextosDeCabecera", PREFIX, PREFIX_URI);
                for(int g=0; g<comprobante.getTextosDeCabecera().getTexto().size(); g++)
                {
                    cabecera.addContent(new Element("Texto", PREFIX, PREFIX_URI).setText(comprobante.getTextosDeCabecera().getTexto().get(g)));
                }
                compro.addContent(cabecera);
            }
            if(comprobante.getTextosDePie()!=null)
            {
                Element cabecera= new Element("TextosDePie", PREFIX, PREFIX_URI);
                for(int g=0; g<comprobante.getTextosDePie().getTexto().size(); g++)
                {
                    cabecera.addContent(new Element("Texto", PREFIX, PREFIX_URI).setText(comprobante.getTextosDePie().getTexto().get(g)));
                }
                compro.addContent(cabecera);
            }
            /*TextosDePie (QUALITAS)*/
            
            /*Mapfre(QUALITAS)*
                ContactoEmisor
                ContactoReceptor
                Poliza
                Vehiculo*/
            if(comprobante.getMapfre()!=null)
            {
                Element mapfre= new Element("Mapfre", PREFIX, PREFIX_URI);
                if(comprobante.getMapfre().getIdAreaAnterior()!=null)
                    mapfre.addContent(new Element("IdAreaAnterior", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getIdAreaAnterior()));
                if(comprobante.getMapfre().getIdArea()!=null)
                    mapfre.addContent(new Element("IdArea", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getIdArea()));
                if(comprobante.getMapfre().getIdRevision()!=null)
                    mapfre.addContent(new Element("IdRevision", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getIdRevision()));
                if(comprobante.getMapfre().getContactoEmisor()!=null)
                {
                    Element contactoEmisor= new Element("ContactoEmisor", PREFIX, PREFIX_URI);
                        if(comprobante.getMapfre().getContactoEmisor().getTipoDeContacto()!=null)
                            contactoEmisor.addContent(new Element("TipoDeContacto", PREFIX, PREFIX_URI).setText("MATRIZ"));
                        contactoEmisor.addContent(new Element("NombreDePersona", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoEmisor().getNombreDePersona()));
                        contactoEmisor.addContent(new Element("EMail", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoEmisor().getEMail()));
                        contactoEmisor.addContent(new Element("Telefono", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoEmisor().getTelefono()));
                        if(comprobante.getMapfre().getContactoEmisor().getExtension()!=null)
                            contactoEmisor.addContent(new Element("Extension", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoEmisor().getExtension()));
                        if(comprobante.getMapfre().getContactoEmisor().getFax()!=null)
                            contactoEmisor.addContent(new Element("Fax", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoEmisor().getFax()));
                    mapfre.addContent(contactoEmisor);
                }
                if(comprobante.getMapfre().getContactoReceptor()!=null)
                {
                    Element contactoReceptor= new Element("ContactoReceptor", PREFIX, PREFIX_URI);
                        if(comprobante.getMapfre().getContactoReceptor().getTipoDeContacto()!=null)
                            contactoReceptor.addContent(new Element("TipoDeContacto", PREFIX, PREFIX_URI).setText("COORDINADOR"));
                        contactoReceptor.addContent(new Element("NombreDePersona", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoReceptor().getNombreDePersona()));
                        contactoReceptor.addContent(new Element("EMail", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoReceptor().getEMail()));
                        contactoReceptor.addContent(new Element("Telefono", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getContactoReceptor().getTelefono()));
                    mapfre.addContent(contactoReceptor);
                }
                if(comprobante.getMapfre().getPoliza()!=null)
                {
                    Element poliza= new Element("Poliza", PREFIX, PREFIX_URI);
                    poliza.addContent(new Element("Tipo", PREFIX, PREFIX_URI).setText("AUTOS"));
                    poliza.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getNumero()));
                    if(comprobante.getMapfre().getPoliza().getInciso()!=null)
                        poliza.addContent(new Element("Inciso", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getInciso()));
                    if(comprobante.getMapfre().getPoliza().getTipoCliente()!=null)
                        poliza.addContent(new Element("TipoCliente", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getTipoCliente()));//0,1,2
                    if(comprobante.getMapfre().getPoliza().getNroReporte()!=null)
                        poliza.addContent(new Element("NroReporte", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getNroReporte()));
                    if(comprobante.getMapfre().getPoliza().getSiniestro()!=null)
                        poliza.addContent(new Element("Siniestro", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getSiniestro()));
                    if(comprobante.getMapfre().getPoliza().getTramitador()!=null)
                        poliza.addContent(new Element("Tramitador", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getTramitador()));
                    if(comprobante.getMapfre().getPoliza().getAsegurado()!=null)
                        poliza.addContent(new Element("Asegurado", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getAsegurado()));
                    if(comprobante.getMapfre().getPoliza().getEndoso()!=null)
                        poliza.addContent(new Element("Endoso", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getEndoso()));
                    if(comprobante.getMapfre().getPoliza().getVigencia()!=null)
                    {
                        Element vigencia= new Element("Vigencia", PREFIX, PREFIX_URI);
                        vigencia.addContent(new Element("Desde", PREFIX, PREFIX_URI).setText(sdf.format(comprobante.getMapfre().getPoliza().getVigencia().getDesde().toGregorianCalendar().getTime())));
                        vigencia.addContent(new Element("Hasta", PREFIX, PREFIX_URI).setText(sdf.format(comprobante.getMapfre().getPoliza().getVigencia().getDesde().toGregorianCalendar().getTime())));
                        poliza.addContent(vigencia);
                    }
                    if(comprobante.getMapfre().getPoliza().getSerieRecibo()!=null)
                        poliza.addContent(new Element("SerieRecibo", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getPoliza().getSerieRecibo()));
                    mapfre.addContent(poliza);
                }
                if(comprobante.getMapfre().getServicio()!=null)
                {
                    Element servicio= new Element("Servicio", PREFIX, PREFIX_URI);
                    servicio.addContent(new Element("Tipo", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getServicio().getTipo()));
                    servicio.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getServicio().getNumero()));
                    mapfre.addContent(servicio);
                }
                if(comprobante.getMapfre().getVehiculo()!=null)
                {
                    Element vehiculo= new Element("Vehiculo", PREFIX, PREFIX_URI);
                    vehiculo.addContent(new Element("Uso", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getUso()));
                    vehiculo.addContent(new Element("Marca", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getMarca()));
                    
                    vehiculo.addContent(new Element("SubMarca", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getMarca()));
                    vehiculo.addContent(new Element("AnoDeProduccion", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getSubMarca()));
                    vehiculo.addContent(new Element("Color", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getColor()));
                    if(comprobante.getMapfre().getVehiculo().getChasis()!=null)
                        vehiculo.addContent(new Element("Chasis", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getChasis()));
                    vehiculo.addContent(new Element("Serie", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getSerie()));
                    if(comprobante.getMapfre().getVehiculo().getMotor()!=null)
                        vehiculo.addContent(new Element("Motor", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getMotor()));
                    vehiculo.addContent(new Element("Placas", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getVehiculo().getPlacas()));
                    mapfre.addContent(vehiculo);
                }
                //local uso para hospitalizacion
                if(comprobante.getMapfre().getImportesAdicionales()!=null)
                {
                    Element importes_adicionales= new Element("ImportesAdicionales", PREFIX, PREFIX_URI);
                    if(comprobante.getMapfre().getImportesAdicionales().getDeducible()!=null)
                        importes_adicionales.addContent(new Element("Deducible", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getImportesAdicionales().getDeducible().getValue().toString()));
                    if(comprobante.getMapfre().getImportesAdicionales().getReaseguro()!=null)
                        importes_adicionales.addContent(new Element("Reaseguro", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getImportesAdicionales().getReaseguro().getValue().toString()));
                    if(comprobante.getMapfre().getImportesAdicionales().getCoaseguro()!=null)
                        importes_adicionales.addContent(new Element("Coaseguro", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getImportesAdicionales().getCoaseguro().getValue().toString()));
                    if(comprobante.getMapfre().getImportesAdicionales().getCoaseguroDeHonorarios()!=null)
                        importes_adicionales.addContent(new Element("CoaseguroDeHonorarios", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getImportesAdicionales().getCoaseguroDeHonorarios().getValue().toString()));
                    mapfre.addContent(importes_adicionales);
                }
                if(comprobante.getMapfre().getAgente()!=null)
                {
                    Element agente= new Element("Agente", PREFIX, PREFIX_URI);
                    agente.addContent(new Element("Clave", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getAgente().getClave()));
                    agente.addContent(new Element("Nombre", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getAgente().getNombre()));
                    mapfre.addContent(agente);
                }
                if(comprobante.getMapfre().getReciboDePrimas()!=null)
                {
                    Element reciboPrimas= new Element("ReciboDePrimas", PREFIX, PREFIX_URI);
                    if(comprobante.getMapfre().getReciboDePrimas().getSerie()!=null)
                        reciboPrimas.addContent(new Element("Serie", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getReciboDePrimas().getSerie()));
                    reciboPrimas.addContent(new Element("Folio", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getReciboDePrimas().getFolio()));
                    if(comprobante.getMapfre().getReciboDePrimas().getExpedidoEn()!=null)
                        reciboPrimas.addContent(new Element("ExpedidoEn", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getReciboDePrimas().getExpedidoEn()));
                    mapfre.addContent(reciboPrimas);
                }
                if(comprobante.getMapfre().getFianza()!=null)
                {
                    Element fianza= new Element("Fianza", PREFIX, PREFIX_URI);
                    fianza.addContent(new Element("Numero", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getFianza().getNumero()));
                    if(comprobante.getMapfre().getFianza().getMonto()!=null)
                        fianza.addContent(new Element("Monto", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getFianza().getMonto().getValue().toString()));
                    if(comprobante.getMapfre().getFianza().getVigencia()!=null)
                    {
                        Element vigencia= new Element("Vigencia", PREFIX, PREFIX_URI);
                        vigencia.addContent(new Element("Desde", PREFIX, PREFIX_URI).setText(sdf.format(comprobante.getMapfre().getFianza().getVigencia().getDesde().toGregorianCalendar().getTime())));
                        vigencia.addContent(new Element("Hasta", PREFIX, PREFIX_URI).setText(sdf.format(comprobante.getMapfre().getFianza().getVigencia().getHasta().toGregorianCalendar().getTime())));
                        fianza.addContent(vigencia);
                    }
                    if(comprobante.getMapfre().getFianza().getObligacion()!=null)
                        fianza.addContent(new Element("Obligacion", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getFianza().getObligacion()));
                    if(comprobante.getMapfre().getFianza().getBeneficiario()!=null)
                        fianza.addContent(new Element("Beneficiario", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getFianza().getBeneficiario()));
                    if(comprobante.getMapfre().getFianza().getMovimiento()!=null)
                        fianza.addContent(new Element("Movimiento", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getFianza().getMovimiento()));
                    mapfre.addContent(fianza);
                }
                if(comprobante.getMapfre().getReaseguradora()!=null)
                {
                    Element reaseguradora= new Element("Reaseguradora", PREFIX, PREFIX_URI);
                    reaseguradora.addContent(new Element("Clave", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getReaseguradora().getClave()));
                    mapfre.addContent(reaseguradora);
                }
                if(comprobante.getMapfre().getOficina()!=null)
                    mapfre.addContent(new Element("Oficina", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getOficina()));
                if(comprobante.getMapfre().getRegistroComision()!=null)
                    mapfre.addContent(new Element("RegistroComision", PREFIX, PREFIX_URI).setText(comprobante.getMapfre().getRegistroComision()));
                /* no aplica para talleres
                Inversiones
                Inmuebles
                TotalesPoliza
                TextosVariables
                TotalesSunsys
                Finamadrid
                Credito
                Agropecuario*/
                compro.addContent(mapfre);
            }
            /*Hotel
            Soriana
            Coppel
            Transportistas
            Automotriz
            Chrysler
            Volkswagen
            ServiciosDeAviacion
            AltosHornos
            EdoCuentaBanco*/
        root.addContent(compro);
    }
    
    public Document getXml() {
        return xml;
    }

    public void setXml(Document xml) {
        this.xml = xml;
    }
    
    public boolean creaAndValidaXML(TFactDocMX comprobante, String nombre){
        boolean response=false;
        generaRaiz(comprobante);
        
        XMLOutputter outputter = new XMLOutputter();
        File folder = new File("nativos");
        folder.mkdirs();
        Format formato = Format.getPrettyFormat();
        formato.setEncoding("UTF-8");
        outputter.setFormat(formato);
        File archivoXml = new File(nombre);
        try 
        {
            //Writer write = new FileWriter(archivoXml);
            FileOutputStream fop = new FileOutputStream(archivoXml);
            outputter.output(getXml(),fop);
        } catch (IOException e) 
        {
            System.err.println("e1:"+e);
            return response;
        }
        //se instancia la clase que validara el XSD
        SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
        builder.setFeature("http://apache.org/xml/features/validation/schema", true);
        builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", COMPROBANTE_SCHEMA_XSD);
        builder.setValidation(true);
        //se imprime el documento si se logro cumplir con el XSD
        try 
        {
            Document document = builder.build(archivoXml);
            //outputter.output(document, System.out);
            response=true;
        } catch (JDOMException e) 
        {
            System.out.println("e2:");
            error=e.toString();
            e.printStackTrace();
        } catch (IOException e) 
        {
            System.out.println("e3");
            error=e.toString();
            e.printStackTrace();
        }
        return response;
    }
}
