//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 09:26:24 AM EDT 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Definition of layout-tag for charts
 * 			
 * 
 * <p>Java class for chartlayout_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="chartlayout_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}componentlayout_type">
 *       &lt;attribute name="border" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *       &lt;attribute name="showlegend" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="backgroundcolor" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#FFF" />
 *       &lt;attribute name="innerbackgroundcolor" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#AAA" />
 *       &lt;attribute name="axescolor" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#555" />
 *       &lt;attribute name="bordercolor" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#000" />
 *       &lt;attribute name="gridcolor" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#555" />
 *       &lt;attribute name="cutpaint" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chartlayout_type")
public class ChartlayoutType
    extends ComponentlayoutType
{

    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger border;
    @XmlAttribute
    protected Boolean showlegend;
    @XmlAttribute
    protected String backgroundcolor;
    @XmlAttribute
    protected String innerbackgroundcolor;
    @XmlAttribute
    protected String axescolor;
    @XmlAttribute
    protected String bordercolor;
    @XmlAttribute
    protected String gridcolor;
    @XmlAttribute
    protected Boolean cutpaint;

    /**
     * Gets the value of the border property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBorder() {
        if (border == null) {
            return new BigInteger("0");
        } else {
            return border;
        }
    }

    /**
     * Sets the value of the border property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBorder(BigInteger value) {
        this.border = value;
    }

    /**
     * Gets the value of the showlegend property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowlegend() {
        if (showlegend == null) {
            return true;
        } else {
            return showlegend;
        }
    }

    /**
     * Sets the value of the showlegend property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowlegend(Boolean value) {
        this.showlegend = value;
    }

    /**
     * Gets the value of the backgroundcolor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackgroundcolor() {
        if (backgroundcolor == null) {
            return "#FFF";
        } else {
            return backgroundcolor;
        }
    }

    /**
     * Sets the value of the backgroundcolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackgroundcolor(String value) {
        this.backgroundcolor = value;
    }

    /**
     * Gets the value of the innerbackgroundcolor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInnerbackgroundcolor() {
        if (innerbackgroundcolor == null) {
            return "#AAA";
        } else {
            return innerbackgroundcolor;
        }
    }

    /**
     * Sets the value of the innerbackgroundcolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInnerbackgroundcolor(String value) {
        this.innerbackgroundcolor = value;
    }

    /**
     * Gets the value of the axescolor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAxescolor() {
        if (axescolor == null) {
            return "#555";
        } else {
            return axescolor;
        }
    }

    /**
     * Sets the value of the axescolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAxescolor(String value) {
        this.axescolor = value;
    }

    /**
     * Gets the value of the bordercolor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBordercolor() {
        if (bordercolor == null) {
            return "#000";
        } else {
            return bordercolor;
        }
    }

    /**
     * Sets the value of the bordercolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBordercolor(String value) {
        this.bordercolor = value;
    }

    /**
     * Gets the value of the gridcolor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGridcolor() {
        if (gridcolor == null) {
            return "#555";
        } else {
            return gridcolor;
        }
    }

    /**
     * Sets the value of the gridcolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGridcolor(String value) {
        this.gridcolor = value;
    }

    /**
     * Gets the value of the cutpaint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCutpaint() {
        if (cutpaint == null) {
            return true;
        } else {
            return cutpaint;
        }
    }

    /**
     * Sets the value of the cutpaint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCutpaint(Boolean value) {
        this.cutpaint = value;
    }

}
