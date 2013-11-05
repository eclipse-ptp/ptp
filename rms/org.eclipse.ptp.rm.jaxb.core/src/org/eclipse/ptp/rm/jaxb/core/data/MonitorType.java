//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.05 at 08:09:06 AM EST 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.eclipse.ptp.rm.jaxb.core.data.lml.LayoutRoot;


/**
 * <p>Java class for monitor-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="monitor-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="driver" type="{http://eclipse.org/ptp/rm}monitor-driver-type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="workflow" type="{http://eclipse.org/ptp/rm}workflow-type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="layout" type="{http://eclipse.org/ptp/lml}layout_root" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="schedulerType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="refreshFrequencyInSeconds" type="{http://www.w3.org/2001/XMLSchema}int" default="60" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "monitor-type", propOrder = {
    "driver",
    "workflow",
    "layout"
})
public class MonitorType {

    protected List<MonitorDriverType> driver;
    protected List<WorkflowType> workflow;
    protected LayoutRoot layout;
    @XmlAttribute(name = "schedulerType")
    protected String schedulerType;
    @XmlAttribute(name = "refreshFrequencyInSeconds")
    protected Integer refreshFrequencyInSeconds;

    /**
     * Gets the value of the driver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the driver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDriver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MonitorDriverType }
     * 
     * 
     */
    public List<MonitorDriverType> getDriver() {
        if (driver == null) {
            driver = new ArrayList<MonitorDriverType>();
        }
        return this.driver;
    }

    /**
     * Gets the value of the workflow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the workflow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorkflow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WorkflowType }
     * 
     * 
     */
    public List<WorkflowType> getWorkflow() {
        if (workflow == null) {
            workflow = new ArrayList<WorkflowType>();
        }
        return this.workflow;
    }

    /**
     * Gets the value of the layout property.
     * 
     * @return
     *     possible object is
     *     {@link LayoutRoot }
     *     
     */
    public LayoutRoot getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     * 
     * @param value
     *     allowed object is
     *     {@link LayoutRoot }
     *     
     */
    public void setLayout(LayoutRoot value) {
        this.layout = value;
    }

    /**
     * Gets the value of the schedulerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchedulerType() {
        return schedulerType;
    }

    /**
     * Sets the value of the schedulerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchedulerType(String value) {
        this.schedulerType = value;
    }

    /**
     * Gets the value of the refreshFrequencyInSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getRefreshFrequencyInSeconds() {
        if (refreshFrequencyInSeconds == null) {
            return  60;
        } else {
            return refreshFrequencyInSeconds;
        }
    }

    /**
     * Sets the value of the refreshFrequencyInSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRefreshFrequencyInSeconds(Integer value) {
        this.refreshFrequencyInSeconds = value;
    }

}
