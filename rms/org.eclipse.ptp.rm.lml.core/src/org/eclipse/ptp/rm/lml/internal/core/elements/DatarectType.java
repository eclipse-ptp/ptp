//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 09:26:24 AM EDT 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *     			One rect in a diagram. Used in timeline-diagram for
 *     			usage-prediction in llview. (x,y) is upper left corner of the rect.
 *     		
 * 
 * <p>Java class for datarect_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="datarect_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}datapoint_type">
 *       &lt;attribute name="w" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="h" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="text" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datarect_type")
public class DatarectType
    extends DatapointType
{

    @XmlAttribute(required = true)
    protected double w;
    @XmlAttribute(required = true)
    protected double h;
    @XmlAttribute
    protected String text;

    /**
     * Gets the value of the w property.
     * 
     */
    public double getW() {
        return w;
    }

    /**
     * Sets the value of the w property.
     * 
     */
    public void setW(double value) {
        this.w = value;
    }

    /**
     * Gets the value of the h property.
     * 
     */
    public double getH() {
        return h;
    }

    /**
     * Sets the value of the h property.
     * 
     */
    public void setH(double value) {
        this.h = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

}
