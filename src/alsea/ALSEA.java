//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2018.11.26 a las 01:20:12 PM CST 
//


package alsea;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numProveedor"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;totalDigits value="15"/&gt;
 *               &lt;minInclusive value="1"/&gt;
 *               &lt;whiteSpace value="collapse"/&gt;
 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="tipoDocumento"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;whiteSpace value="collapse"/&gt;
 *               &lt;totalDigits value="1"/&gt;
 *               &lt;minInclusive value="1"/&gt;
 *               &lt;maxInclusive value="3"/&gt;
 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *               &lt;enumeration value="2"/&gt;
 *               &lt;enumeration value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="moneda"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;whiteSpace value="collapse"/&gt;
 *               &lt;minLength value="3"/&gt;
 *               &lt;maxLength value="3"/&gt;
 *               &lt;enumeration value="MXN"/&gt;
 *               &lt;enumeration value="USD"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="informacion"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="ordenDeCompra"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="numOrden"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                   &lt;totalDigits value="15"/&gt;
 *                                   &lt;minInclusive value="1"/&gt;
 *                                   &lt;whiteSpace value="collapse"/&gt;
 *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="detalle"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="concepto" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;sequence&gt;
 *                                                 &lt;element name="linea"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="envio"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                               &lt;/sequence&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="acuerdoDeCompra"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="numAcuerdo"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                   &lt;whiteSpace value="collapse"/&gt;
 *                                   &lt;minInclusive value="1"/&gt;
 *                                   &lt;totalDigits value="15"/&gt;
 *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="detalle"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="concepto" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;sequence&gt;
 *                                                 &lt;element name="release"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="linea"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="envio"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                               &lt;/sequence&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="acuerdoDeCompraPorRecepcion"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="numAcuerdo"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                   &lt;totalDigits value="15"/&gt;
 *                                   &lt;minInclusive value="1"/&gt;
 *                                   &lt;whiteSpace value="collapse"/&gt;
 *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="detalle"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="concepto" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;sequence&gt;
 *                                                 &lt;element name="release"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="linea"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="envio"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                                       &lt;totalDigits value="5"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                       &lt;pattern value="[\-+]?[0-9]+"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="cantidad"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                                       &lt;fractionDigits value="2"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;totalDigits value="15"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                                 &lt;element name="precio"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                                       &lt;fractionDigits value="2"/&gt;
 *                                                       &lt;minInclusive value="1"/&gt;
 *                                                       &lt;totalDigits value="15"/&gt;
 *                                                       &lt;whiteSpace value="collapse"/&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/element&gt;
 *                                               &lt;/sequence&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "numProveedor",
    "tipoDocumento",
    "moneda",
    "informacion"
})
@XmlRootElement(name = "ALSEA")
public class ALSEA {

    @XmlElement(required = true)
    protected BigInteger numProveedor=BigInteger.ZERO;
    protected int tipoDocumento= 1;
    @XmlElement(required = true)
    protected String moneda="MXN";
    @XmlElement(required = true)
    protected ALSEA.Informacion informacion= new ALSEA.Informacion();

    /**
     * Obtiene el valor de la propiedad numProveedor.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumProveedor() {
        return numProveedor;
    }

    /**
     * Define el valor de la propiedad numProveedor.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumProveedor(BigInteger value) {
        this.numProveedor = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumento.
     * 
     */
    public int getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Define el valor de la propiedad tipoDocumento.
     * 
     */
    public void setTipoDocumento(int value) {
        this.tipoDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoneda(String value) {
        this.moneda = value;
    }

    /**
     * Obtiene el valor de la propiedad informacion.
     * 
     * @return
     *     possible object is
     *     {@link ALSEA.Informacion }
     *     
     */
    public ALSEA.Informacion getInformacion() {
        return informacion;
    }

    /**
     * Define el valor de la propiedad informacion.
     * 
     * @param value
     *     allowed object is
     *     {@link ALSEA.Informacion }
     *     
     */
    public void setInformacion(ALSEA.Informacion value) {
        this.informacion = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element name="ordenDeCompra"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="numOrden"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                         &lt;totalDigits value="15"/&gt;
     *                         &lt;minInclusive value="1"/&gt;
     *                         &lt;whiteSpace value="collapse"/&gt;
     *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="detalle"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="concepto" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="linea"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="envio"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="acuerdoDeCompra"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="numAcuerdo"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                         &lt;whiteSpace value="collapse"/&gt;
     *                         &lt;minInclusive value="1"/&gt;
     *                         &lt;totalDigits value="15"/&gt;
     *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="detalle"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="concepto" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="release"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="linea"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="envio"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="acuerdoDeCompraPorRecepcion"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="numAcuerdo"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                         &lt;totalDigits value="15"/&gt;
     *                         &lt;minInclusive value="1"/&gt;
     *                         &lt;whiteSpace value="collapse"/&gt;
     *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="detalle"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="concepto" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="release"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="linea"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="envio"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                             &lt;totalDigits value="5"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                             &lt;pattern value="[\-+]?[0-9]+"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="cantidad"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                                             &lt;fractionDigits value="2"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;totalDigits value="15"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="precio"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                                             &lt;fractionDigits value="2"/&gt;
     *                                             &lt;minInclusive value="1"/&gt;
     *                                             &lt;totalDigits value="15"/&gt;
     *                                             &lt;whiteSpace value="collapse"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "ordenDeCompra",
        "acuerdoDeCompra",
        "acuerdoDeCompraPorRecepcion"
    })
    public static class Informacion {

        protected ALSEA.Informacion.OrdenDeCompra ordenDeCompra;
        protected ALSEA.Informacion.AcuerdoDeCompra acuerdoDeCompra;
        protected ALSEA.Informacion.AcuerdoDeCompraPorRecepcion acuerdoDeCompraPorRecepcion;

        /**
         * Obtiene el valor de la propiedad ordenDeCompra.
         * 
         * @return
         *     possible object is
         *     {@link ALSEA.Informacion.OrdenDeCompra }
         *     
         */
        public ALSEA.Informacion.OrdenDeCompra getOrdenDeCompra() {
            if(ordenDeCompra==null)
                ordenDeCompra=new ALSEA.Informacion.OrdenDeCompra();
            return ordenDeCompra;
        }

        /**
         * Define el valor de la propiedad ordenDeCompra.
         * 
         * @param value
         *     allowed object is
         *     {@link ALSEA.Informacion.OrdenDeCompra }
         *     
         */
        public void setOrdenDeCompra(ALSEA.Informacion.OrdenDeCompra value) {
            this.ordenDeCompra = value;
        }

        /**
         * Obtiene el valor de la propiedad acuerdoDeCompra.
         * 
         * @return
         *     possible object is
         *     {@link ALSEA.Informacion.AcuerdoDeCompra }
         *     
         */
        public ALSEA.Informacion.AcuerdoDeCompra getAcuerdoDeCompra() {
            if(acuerdoDeCompra==null)
                acuerdoDeCompra=new ALSEA.Informacion.AcuerdoDeCompra();
            return acuerdoDeCompra;
        }

        /**
         * Define el valor de la propiedad acuerdoDeCompra.
         * 
         * @param value
         *     allowed object is
         *     {@link ALSEA.Informacion.AcuerdoDeCompra }
         *     
         */
        public void setAcuerdoDeCompra(ALSEA.Informacion.AcuerdoDeCompra value) {
            this.acuerdoDeCompra = value;
        }

        /**
         * Obtiene el valor de la propiedad acuerdoDeCompraPorRecepcion.
         * 
         * @return
         *     possible object is
         *     {@link ALSEA.Informacion.AcuerdoDeCompraPorRecepcion }
         *     
         */
        public ALSEA.Informacion.AcuerdoDeCompraPorRecepcion getAcuerdoDeCompraPorRecepcion() {
            if(acuerdoDeCompraPorRecepcion==null)
                acuerdoDeCompraPorRecepcion=new ALSEA.Informacion.AcuerdoDeCompraPorRecepcion();
            return acuerdoDeCompraPorRecepcion;
        }

        /**
         * Define el valor de la propiedad acuerdoDeCompraPorRecepcion.
         * 
         * @param value
         *     allowed object is
         *     {@link ALSEA.Informacion.AcuerdoDeCompraPorRecepcion }
         *     
         */
        public void setAcuerdoDeCompraPorRecepcion(ALSEA.Informacion.AcuerdoDeCompraPorRecepcion value) {
            this.acuerdoDeCompraPorRecepcion = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="numAcuerdo"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *               &lt;whiteSpace value="collapse"/&gt;
         *               &lt;minInclusive value="1"/&gt;
         *               &lt;totalDigits value="15"/&gt;
         *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="detalle"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="concepto" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="release"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="linea"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="envio"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "numAcuerdo",
            "detalle"
        })
        public static class AcuerdoDeCompra {

            @XmlElement(required = true)
            protected BigInteger numAcuerdo;
            @XmlElement(required = true)
            protected ALSEA.Informacion.AcuerdoDeCompra.Detalle detalle;

            /**
             * Obtiene el valor de la propiedad numAcuerdo.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNumAcuerdo() {
                return numAcuerdo;
            }

            /**
             * Define el valor de la propiedad numAcuerdo.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNumAcuerdo(BigInteger value) {
                this.numAcuerdo = value;
            }

            /**
             * Obtiene el valor de la propiedad detalle.
             * 
             * @return
             *     possible object is
             *     {@link ALSEA.Informacion.AcuerdoDeCompra.Detalle }
             *     
             */
            public ALSEA.Informacion.AcuerdoDeCompra.Detalle getDetalle() {
                return detalle;
            }

            /**
             * Define el valor de la propiedad detalle.
             * 
             * @param value
             *     allowed object is
             *     {@link ALSEA.Informacion.AcuerdoDeCompra.Detalle }
             *     
             */
            public void setDetalle(ALSEA.Informacion.AcuerdoDeCompra.Detalle value) {
                this.detalle = value;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="concepto" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="release"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="linea"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="envio"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "concepto"
            })
            public static class Detalle {

                @XmlElement(required = true)
                protected List<ALSEA.Informacion.AcuerdoDeCompra.Detalle.Concepto> concepto;

                /**
                 * Gets the value of the concepto property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the concepto property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getConcepto().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ALSEA.Informacion.AcuerdoDeCompra.Detalle.Concepto }
                 * 
                 * 
                 */
                public List<ALSEA.Informacion.AcuerdoDeCompra.Detalle.Concepto> getConcepto() {
                    if (concepto == null) {
                        concepto = new ArrayList<ALSEA.Informacion.AcuerdoDeCompra.Detalle.Concepto>();
                    }
                    return this.concepto;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="release"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="linea"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="envio"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "release",
                    "linea",
                    "envio"
                })
                public static class Concepto {

                    @XmlElement(required = true)
                    protected BigInteger release;
                    @XmlElement(required = true)
                    protected BigInteger linea;
                    @XmlElement(required = true)
                    protected BigInteger envio;

                    /**
                     * Obtiene el valor de la propiedad release.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getRelease() {
                        return release;
                    }

                    /**
                     * Define el valor de la propiedad release.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setRelease(BigInteger value) {
                        this.release = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad linea.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getLinea() {
                        return linea;
                    }

                    /**
                     * Define el valor de la propiedad linea.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setLinea(BigInteger value) {
                        this.linea = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad envio.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getEnvio() {
                        return envio;
                    }

                    /**
                     * Define el valor de la propiedad envio.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setEnvio(BigInteger value) {
                        this.envio = value;
                    }

                }

            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="numAcuerdo"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *               &lt;totalDigits value="15"/&gt;
         *               &lt;minInclusive value="1"/&gt;
         *               &lt;whiteSpace value="collapse"/&gt;
         *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="detalle"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="concepto" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="release"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="linea"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="envio"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="cantidad"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *                                   &lt;fractionDigits value="2"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;totalDigits value="15"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="precio"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *                                   &lt;fractionDigits value="2"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;totalDigits value="15"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "numAcuerdo",
            "detalle"
        })
        public static class AcuerdoDeCompraPorRecepcion {

            @XmlElement(required = true)
            protected BigInteger numAcuerdo;
            @XmlElement(required = true)
            protected ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle detalle;

            /**
             * Obtiene el valor de la propiedad numAcuerdo.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNumAcuerdo() {
                return numAcuerdo;
            }

            /**
             * Define el valor de la propiedad numAcuerdo.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNumAcuerdo(BigInteger value) {
                this.numAcuerdo = value;
            }

            /**
             * Obtiene el valor de la propiedad detalle.
             * 
             * @return
             *     possible object is
             *     {@link ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle }
             *     
             */
            public ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle getDetalle() {
                return detalle;
            }

            /**
             * Define el valor de la propiedad detalle.
             * 
             * @param value
             *     allowed object is
             *     {@link ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle }
             *     
             */
            public void setDetalle(ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle value) {
                this.detalle = value;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="concepto" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="release"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="linea"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="envio"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="cantidad"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
             *                         &lt;fractionDigits value="2"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;totalDigits value="15"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="precio"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
             *                         &lt;fractionDigits value="2"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;totalDigits value="15"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "concepto"
            })
            public static class Detalle {

                @XmlElement(required = true)
                protected List<ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle.Concepto> concepto;

                /**
                 * Gets the value of the concepto property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the concepto property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getConcepto().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle.Concepto }
                 * 
                 * 
                 */
                public List<ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle.Concepto> getConcepto() {
                    if (concepto == null) {
                        concepto = new ArrayList<ALSEA.Informacion.AcuerdoDeCompraPorRecepcion.Detalle.Concepto>();
                    }
                    return this.concepto;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="release"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="linea"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="envio"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="cantidad"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
                 *               &lt;fractionDigits value="2"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;totalDigits value="15"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="precio"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
                 *               &lt;fractionDigits value="2"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;totalDigits value="15"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "release",
                    "linea",
                    "envio",
                    "cantidad",
                    "precio"
                })
                public static class Concepto {

                    @XmlElement(required = true)
                    protected BigInteger release;
                    @XmlElement(required = true)
                    protected BigInteger linea;
                    @XmlElement(required = true)
                    protected BigInteger envio;
                    @XmlElement(required = true)
                    protected BigDecimal cantidad;
                    @XmlElement(required = true)
                    protected BigDecimal precio;

                    /**
                     * Obtiene el valor de la propiedad release.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getRelease() {
                        return release;
                    }

                    /**
                     * Define el valor de la propiedad release.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setRelease(BigInteger value) {
                        this.release = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad linea.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getLinea() {
                        return linea;
                    }

                    /**
                     * Define el valor de la propiedad linea.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setLinea(BigInteger value) {
                        this.linea = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad envio.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getEnvio() {
                        return envio;
                    }

                    /**
                     * Define el valor de la propiedad envio.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setEnvio(BigInteger value) {
                        this.envio = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad cantidad.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigDecimal }
                     *     
                     */
                    public BigDecimal getCantidad() {
                        return cantidad;
                    }

                    /**
                     * Define el valor de la propiedad cantidad.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigDecimal }
                     *     
                     */
                    public void setCantidad(BigDecimal value) {
                        this.cantidad = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad precio.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigDecimal }
                     *     
                     */
                    public BigDecimal getPrecio() {
                        return precio;
                    }

                    /**
                     * Define el valor de la propiedad precio.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigDecimal }
                     *     
                     */
                    public void setPrecio(BigDecimal value) {
                        this.precio = value;
                    }

                }

            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="numOrden"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *               &lt;totalDigits value="15"/&gt;
         *               &lt;minInclusive value="1"/&gt;
         *               &lt;whiteSpace value="collapse"/&gt;
         *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="detalle"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="concepto" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="linea"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="envio"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                                   &lt;totalDigits value="5"/&gt;
         *                                   &lt;minInclusive value="1"/&gt;
         *                                   &lt;whiteSpace value="collapse"/&gt;
         *                                   &lt;pattern value="[\-+]?[0-9]+"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "numOrden",
            "detalle"
        })
        public static class OrdenDeCompra {

            @XmlElement(required = true)
            protected BigInteger numOrden;
            @XmlElement(required = true)
            protected ALSEA.Informacion.OrdenDeCompra.Detalle detalle;

            /**
             * Obtiene el valor de la propiedad numOrden.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNumOrden() {
                return numOrden;
            }

            /**
             * Define el valor de la propiedad numOrden.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNumOrden(BigInteger value) {
                this.numOrden = value;
            }

            /**
             * Obtiene el valor de la propiedad detalle.
             * 
             * @return
             *     possible object is
             *     {@link ALSEA.Informacion.OrdenDeCompra.Detalle }
             *     
             */
            public ALSEA.Informacion.OrdenDeCompra.Detalle getDetalle() {
                if(detalle==null)
                    detalle=new ALSEA.Informacion.OrdenDeCompra.Detalle();
                return detalle;
            }

            /**
             * Define el valor de la propiedad detalle.
             * 
             * @param value
             *     allowed object is
             *     {@link ALSEA.Informacion.OrdenDeCompra.Detalle }
             *     
             */
            public void setDetalle(ALSEA.Informacion.OrdenDeCompra.Detalle value) {
                this.detalle = value;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="concepto" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="linea"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="envio"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *                         &lt;totalDigits value="5"/&gt;
             *                         &lt;minInclusive value="1"/&gt;
             *                         &lt;whiteSpace value="collapse"/&gt;
             *                         &lt;pattern value="[\-+]?[0-9]+"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "concepto"
            })
            public static class Detalle {

                @XmlElement(required = true)
                protected List<ALSEA.Informacion.OrdenDeCompra.Detalle.Concepto> concepto;

                /**
                 * Gets the value of the concepto property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the concepto property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getConcepto().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ALSEA.Informacion.OrdenDeCompra.Detalle.Concepto }
                 * 
                 * 
                 */
                public List<ALSEA.Informacion.OrdenDeCompra.Detalle.Concepto> getConcepto() {
                    if (concepto == null) {
                        concepto = new ArrayList<ALSEA.Informacion.OrdenDeCompra.Detalle.Concepto>();
                    }
                    return this.concepto;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="linea"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="envio"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
                 *               &lt;totalDigits value="5"/&gt;
                 *               &lt;minInclusive value="1"/&gt;
                 *               &lt;whiteSpace value="collapse"/&gt;
                 *               &lt;pattern value="[\-+]?[0-9]+"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "linea",
                    "envio"
                })
                public static class Concepto {

                    @XmlElement(required = true)
                    protected BigInteger linea;
                    @XmlElement(required = true)
                    protected BigInteger envio;

                    /**
                     * Obtiene el valor de la propiedad linea.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getLinea() {
                        return linea;
                    }

                    /**
                     * Define el valor de la propiedad linea.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setLinea(BigInteger value) {
                        this.linea = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad envio.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getEnvio() {
                        return envio;
                    }

                    /**
                     * Define el valor de la propiedad envio.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setEnvio(BigInteger value) {
                        this.envio = value;
                    }

                }

            }

        }

    }

}
