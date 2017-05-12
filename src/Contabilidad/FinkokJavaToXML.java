/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Contabilidad;

/**
 *
 * @author salvador
 */
import finkok.Comprobante.Conceptos;
import finkok.Comprobante.Conceptos.Concepto;
import finkok.Comprobante.Emisor;
import finkok.Comprobante.Impuestos;
import finkok.Comprobante.Receptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import timbrar.Constantes;



/**
 *
 * @author eder
 */
public class FinkokJavaToXML {
    protected Locale local = new java.util.Locale (Constantes.LOCALE_LANGUAGE, Constantes.LOCALE_COUNTRY, Constantes.LOCALE_VARIANT);
    protected SimpleDateFormat sdf = new SimpleDateFormat (Constantes.DATE_SHORT_HUMAN_PATTERN, local);
    private Document xml=new Document();
    private Element root;
/**
 * Este metodo genera el Elemento Comprobante del XML
 * @param comprobante
 */
    public void generaRaiz(finkok.Comprobante comprobante) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //crea un elemento raiz y le asigna los namespaces y los xsd
        root = new Element(Constantes.COMPROBANTE_COMPROBANTE,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        //root = new Element(Constantes.COMPROBANTE_COMPROBANTE,Constantes.COMPROBANTE_NAMESPACE_URI);
        Namespace namespace=Namespace.getNamespace(Constantes.COMPROBANTE_PREFIX, Constantes.COMPROBANTE_URI_PREFIX);
        root.setAttribute(Constantes.COMPROBANTE_SCHEMA,Constantes.COMPROBANTE_SCHEMA_XSD,namespace);
        root.addNamespaceDeclaration(namespace);
        //se empieza a construir el comprobante
        root.setAttribute(Constantes.COMPROBANTE_VERSION, Constantes.VERSION_COMPROBANTE_TRES);
        if(comprobante.getSerie()!=null&&!comprobante.getSerie().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_SERIE, comprobante.getSerie());
        }
        root.setAttribute(Constantes.COMPROBANTE_FECHA, sdf.format(comprobante.getFecha().toGregorianCalendar().getTime()));
        root.setAttribute(Constantes.COMPROBANTE_SELLO, comprobante.getSello());
        if(comprobante.getFormaDePago()!=null&&!comprobante.getFormaDePago().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_FORMA_PAGO, comprobante.getFormaDePago());
        }
        root.setAttribute(Constantes.COMPROBANTE_NO_CERTIFICADO, comprobante.getNoCertificado());
        root.setAttribute(Constantes.COMPROBANTE_CERTIFICADO, comprobante.getCertificado());
        if(comprobante.getCondicionesDePago()!=null&&!comprobante.getCondicionesDePago().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_CONDICIONES_PAGO, acentos(comprobante.getCondicionesDePago()));
        }
        root.setAttribute(Constantes.COMPROBANTE_SUBTOTAL, comprobante.getSubTotal().setScale(6, RoundingMode.HALF_UP).toString());
        if((comprobante.getDescuento()!=null&&comprobante.getDescuento()!=null)&&!comprobante.getDescuento().setScale(6, RoundingMode.HALF_UP).toString().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_DESCUENTO, comprobante.getDescuento().setScale(6, RoundingMode.HALF_UP).toString());
        }
        if(comprobante.getMotivoDescuento()!=null&&!comprobante.getMotivoDescuento().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_MOTIVO_DESCUENTO, acentos(comprobante.getMotivoDescuento()));
        }
        if(comprobante.getTipoCambio()!=null&&!comprobante.getTipoCambio().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_TIPO_CAMBIO, comprobante.getTipoCambio());
        }
        if(comprobante.getMoneda()!=null&&!comprobante.getMoneda().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_MONEDA, comprobante.getMoneda());
        }
        root.setAttribute(Constantes.COMPROBANTE_TOTAL, comprobante.getTotal().setScale(6, RoundingMode.HALF_UP).toString());
        root.setAttribute(Constantes.TIPO_DE_COMPROBANTE, comprobante.getTipoDeComprobante());
        //root.setAttribute(Constantes.COMPROBANTE_NO_APROBACION, comprobante.getNoAprobacion().toString());
        //root.setAttribute(Constantes.COMPROBANTE_ANO_APROBACION, comprobante.getAnoAprobacion().toString());
        if(comprobante.getMetodoDePago()!=null&&!comprobante.getMetodoDePago().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_METODO_DE_PAGO, acentos(comprobante.getMetodoDePago()));
        }
        //root.setAttribute(Constantes.COMPROBANTE_METODO_DE_PAGO, comprobante.getMetodoDePago());
        root.setAttribute(Constantes.COMPROBANTE_LUGAR_DE_EXPEDICION, acentos(comprobante.getLugarExpedicion()));
        //root.setAttribute(Constantes.COMPROBANTE_REGIMEN_FISCAL, comprobante.getLugarExpedicion());
        //se generan el Nodo Emisor
        if(comprobante.getFolio()!=null&&!comprobante.getFolio().trim().equals("")){
        root.setAttribute(Constantes.COMPROBANTE_FOLIO, comprobante.getFolio());
        }
        generaEmisor(comprobante.getEmisor());
        generaReceptor(comprobante.getReceptor());
        generaConceptos(comprobante.getConceptos());
        generaImpuestos(comprobante.getImpuestos());
        getXml().setRootElement(root);
    }

    public void generaEmisor(Emisor emisor) {
        //se le agrega el namespace al emisor
        Element emisorElement = new Element(Constantes.COMPROBANTE_EMISOR,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        emisorElement.setAttribute(Constantes.COMPROBANTE_RFC, emisor.getRfc());
        emisorElement.setAttribute(Constantes.COMPROBANTE_NOMBRE, acentos(emisor.getNombre()));
        //se crea el nodo domiciliofiscal
        Element domicilioFiscal = new Element(Constantes.COMPROBANTE_DOMICILIO_FISCAL,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_CALLE, acentos(emisor.getDomicilioFiscal().getCalle()));

        if(emisor.getDomicilioFiscal().getNoExterior()!=null&&!emisor.getDomicilioFiscal().getNoExterior().trim().equals("")){
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_NO_EXTERIOR, emisor.getDomicilioFiscal().getNoExterior());
        }
        if(emisor.getDomicilioFiscal().getNoInterior()!=null&&!emisor.getDomicilioFiscal().getNoInterior().trim().equals("")){
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_NO_INTERIOR, emisor.getDomicilioFiscal().getNoInterior());
        }
        if(emisor.getDomicilioFiscal().getColonia()!=null&&!emisor.getDomicilioFiscal().getColonia().trim().equals("")){
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_COLONIA, acentos(emisor.getDomicilioFiscal().getColonia()));
        }
        if(emisor.getDomicilioFiscal().getLocalidad()!=null&&!emisor.getDomicilioFiscal().getLocalidad().trim().equals("")){
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_LOCALIDAD, acentos(emisor.getDomicilioFiscal().getLocalidad()));
        }
        if(emisor.getDomicilioFiscal().getReferencia()!=null&&!emisor.getDomicilioFiscal().getReferencia().trim().equals("")){
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_REFERENCIA, acentos(emisor.getDomicilioFiscal().getReferencia()));
        }
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_MUNICIPIO, acentos(emisor.getDomicilioFiscal().getMunicipio()));
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_ESTADO, acentos(emisor.getDomicilioFiscal().getEstado()));
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_PAIS, acentos(emisor.getDomicilioFiscal().getPais()));
        domicilioFiscal.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_CODIGO_POSTAL, acentos(emisor.getDomicilioFiscal().getCodigoPostal()));
        //se agrega el domicilio fiscal al nodo emisor
        emisorElement.addContent(domicilioFiscal);
        
        Element RegimenFiscal = new Element(Constantes.COMPROBANTE_REGIMEN_FISCAL,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        RegimenFiscal.setAttribute(Constantes.COMPROBANTE_REGIMEN, acentos(emisor.getRegimenFiscal().get(0).getRegimen()));
        emisorElement.addContent(RegimenFiscal);
        //se agrega el nodo emisor al elemento comprobante
        root.addContent(emisorElement);
    }

    public void generaReceptor(Receptor receptor) {
        //se crea el nodo receptor
        Element rec = new Element(Constantes.COMPROBANTE_RECEPTOR,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        rec.setAttribute(Constantes.COMPROBANTE_RFC, receptor.getRfc());
        if(receptor.getNombre()!=null&&!receptor.getNombre().trim().equals("")){
        rec.setAttribute(Constantes.COMPROBANTE_NOMBRE, acentos(receptor.getNombre()));
        }
        //se crea el nodo domicilio
        Element domicilio = new Element(Constantes.COMPROBANTE_DOMICILIO,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);

        if(receptor.getDomicilio().getCalle()!=null&&!receptor.getDomicilio().getCalle().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_CALLE, acentos(receptor.getDomicilio().getCalle()));
        }
        if(receptor.getDomicilio().getNoExterior()!=null&&!receptor.getDomicilio().getNoExterior().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_NO_EXTERIOR, receptor.getDomicilio().getNoExterior());
        }
        if(receptor.getDomicilio().getNoInterior()!=null&&!receptor.getDomicilio().getNoInterior().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_NO_INTERIOR, receptor.getDomicilio().getNoInterior());
        }
        if(receptor.getDomicilio().getColonia()!=null&&!receptor.getDomicilio().getColonia().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_COLONIA, acentos(receptor.getDomicilio().getColonia()));
        }
        if(receptor.getDomicilio().getLocalidad()!=null&&!receptor.getDomicilio().getLocalidad().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_LOCALIDAD, acentos(receptor.getDomicilio().getLocalidad()));
        }
        if(receptor.getDomicilio().getReferencia()!=null&&!receptor.getDomicilio().getReferencia().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_REFERENCIA, acentos(receptor.getDomicilio().getReferencia()));
        }
        if(receptor.getDomicilio().getMunicipio()!=null&&!receptor.getDomicilio().getMunicipio().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_MUNICIPIO, acentos(receptor.getDomicilio().getMunicipio()));
        }
        if(receptor.getDomicilio().getEstado()!=null&&!receptor.getDomicilio().getEstado().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_ESTADO, acentos(receptor.getDomicilio().getEstado()));
        }
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_PAIS, acentos(receptor.getDomicilio().getPais()));
        if(receptor.getDomicilio().getCodigoPostal()!=null&&!receptor.getDomicilio().getCodigoPostal().trim().equals("")){
        domicilio.setAttribute(Constantes.COMPROBANTE_DOMICILIO_GENERICO_CODIGO_POSTAL, receptor.getDomicilio().getCodigoPostal());
        }
        //se agrega el domicilio al receptor
        rec.addContent(domicilio);
        //se agrega el nodo receptor al comprobante
        root.addContent(rec);
    }

    public void generaConceptos(Conceptos concp) {
        Element conceptos = new Element(Constantes.COMPROBANTE_CONCEPTOS,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        for (Concepto con : concp.getConcepto()) {
            Element concepto = new Element(Constantes.COMPROBANTE_CONCEPTO,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
            concepto.setAttribute(Constantes.COMPROBANTE_CONCEPTO_CANTIDAD, con.getCantidad().setScale(6, RoundingMode.HALF_UP).toString());
            if(con.getUnidad()!=null&&!con.getUnidad().trim().equals("")){
            concepto.setAttribute(Constantes.COMPROBANTE_CONCEPTO_UNIDAD, con.getUnidad());
            }
            if(con.getNoIdentificacion()!=null&&!con.getNoIdentificacion().trim().equals("")){
            concepto.setAttribute(Constantes.COMPROBANTE_CONCEPTO_NO_IDENTIFICACION, con.getNoIdentificacion());
            }
            concepto.setAttribute(Constantes.COMPROBANTE_CONCEPTO_DESCRIPCION, acentos(con.getDescripcion()));
            concepto.setAttribute(Constantes.COMPROBANTE_CONCEPTO_VALOR_UNITARIO, con.getValorUnitario().setScale(6, RoundingMode.HALF_UP).toString());
            concepto.setAttribute(Constantes.COMPROBANTE_CONCEPTO_IMPORTE, con.getImporte().setScale(6, RoundingMode.HALF_UP).toString());
            //checar los nodos opcionales de conceptos InformacionAduanera,CuentaPredial,ComplementoConcepto,Parte
            conceptos.addContent(concepto);
        }
        root.addContent(conceptos);
    }

    public void generaImpuestos(Impuestos imp){
        //checar los elementos de impuestos
        Element impuestos=new Element(Constantes.COMPROBANTE_IMPUESTOS, Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        impuestos.setAttribute(Constantes.COMPROBANTE_IMPUESTOS_TOTAL_IMPUESTOS, ""+imp.getTotalImpuestosTrasladados().setScale(6, RoundingMode.HALF_UP));
        Element Traslados = new Element(Constantes.COMPROBANTE_TRASLADOS,Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        Element Traslado = new Element("Traslado",Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
        Traslado.setAttribute("impuesto", imp.getTraslados().getTraslado().get(0).getImpuesto());
        Traslado.setAttribute("tasa", ""+imp.getTraslados().getTraslado().get(0).getTasa().setScale(0, RoundingMode.HALF_UP));
        Traslado.setAttribute("importe", ""+imp.getTraslados().getTraslado().get(0).getImporte().setScale(6, RoundingMode.HALF_UP));
        Traslados.addContent(Traslado);
        impuestos.addContent(Traslados);
        root.addContent(impuestos);
    }

    public boolean creaAndValidaXML(finkok.Comprobante comprobante, String archivo){
        boolean response=false;
        generaRaiz(comprobante);
        
        XMLOutputter outputter = new XMLOutputter();
        File folder = new File("nativos");
        folder.mkdirs();
        Format formato = Format.getPrettyFormat();
        formato.setEncoding("UTF-8");
        outputter.setFormat(formato);
        File archivoXml = new File(archivo);
        if(archivoXml.exists()==true)
            archivoXml.delete();
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
        builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", Constantes.COMPROBANTE_SCHEMA_XSD);
        builder.setValidation(true);
        //se imprime el documento si se logro cumplir con el XSD
        //se imprime el documento si se logro cumplir con el XSD
        try 
        {
            Document document = builder.build(archivoXml);
            //outputter.output(document, System.out);
            response=true;
        } catch (JDOMException e) 
        {
            System.out.println("e2:");
            e.printStackTrace();
        } catch (IOException e) 
        {
            System.out.println("e3");
            e.printStackTrace();
        }
        
        return response;
    }

    /**
     * @return the xml
     */
    public Document getXml() {
        return xml;
    }

    /**
     * @param xml the xml to set
     */
    public void setXml(Document xml) {
        this.xml = xml;
    }
 
    public static String acentos(String input) {
        // Descomposición canónica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos únicamente con los caracteres ASCII
        Pattern pattern = Pattern.compile("\\P{ASCII}");
        return pattern.matcher(normalized).replaceAll("");
    }
}