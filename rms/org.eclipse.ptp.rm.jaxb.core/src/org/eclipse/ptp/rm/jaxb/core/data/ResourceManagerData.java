//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.02.12 at 06:02:35 PM EST 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resource-manager-data complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource-manager-data">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="site-data" type="{org.eclipse.ptp}site-type" minOccurs="0"/>
 *         &lt;element name="control-data" type="{org.eclipse.ptp}control-type" minOccurs="0"/>
 *         &lt;element name="monitor-data" type="{org.eclipse.ptp}monitor-type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resource-manager-data", propOrder = {
    "siteData",
    "controlData",
    "monitorData"
})
public class ResourceManagerData {

    @XmlElement(name = "site-data")
    protected SiteType siteData;
    @XmlElement(name = "control-data")
    protected ControlType controlData;
    @XmlElement(name = "monitor-data")
    protected MonitorType monitorData;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the siteData property.
     * 
     * @return
     *     possible object is
     *     {@link SiteType }
     *     
     */
    public SiteType getSiteData() {
        return siteData;
    }

    /**
     * Sets the value of the siteData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SiteType }
     *     
     */
    public void setSiteData(SiteType value) {
        this.siteData = value;
    }

    /**
     * Gets the value of the controlData property.
     * 
     * @return
     *     possible object is
     *     {@link ControlType }
     *     
     */
    public ControlType getControlData() {
        return controlData;
    }

    /**
     * Sets the value of the controlData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlType }
     *     
     */
    public void setControlData(ControlType value) {
        this.controlData = value;
    }

    /**
     * Gets the value of the monitorData property.
     * 
     * @return
     *     possible object is
     *     {@link MonitorType }
     *     
     */
    public MonitorType getMonitorData() {
        return monitorData;
    }

    /**
     * Sets the value of the monitorData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitorType }
     *     
     */
    public void setMonitorData(MonitorType value) {
        this.monitorData = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
