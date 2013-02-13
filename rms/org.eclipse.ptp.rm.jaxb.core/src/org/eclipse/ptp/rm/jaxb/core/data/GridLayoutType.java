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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grid-layout-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="grid-layout-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="numColumns" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="makeColumnsEqualWidth" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="marginHeight" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginWidth" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginTop" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginBottom" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginLeft" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginRight" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="horizontalSpacing" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="verticalSpacing" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grid-layout-type")
public class GridLayoutType {

    @XmlAttribute(name = "numColumns")
    protected Integer numColumns;
    @XmlAttribute(name = "makeColumnsEqualWidth")
    protected Boolean makeColumnsEqualWidth;
    @XmlAttribute(name = "marginHeight")
    protected Integer marginHeight;
    @XmlAttribute(name = "marginWidth")
    protected Integer marginWidth;
    @XmlAttribute(name = "marginTop")
    protected Integer marginTop;
    @XmlAttribute(name = "marginBottom")
    protected Integer marginBottom;
    @XmlAttribute(name = "marginLeft")
    protected Integer marginLeft;
    @XmlAttribute(name = "marginRight")
    protected Integer marginRight;
    @XmlAttribute(name = "horizontalSpacing")
    protected Integer horizontalSpacing;
    @XmlAttribute(name = "verticalSpacing")
    protected Integer verticalSpacing;

    /**
     * Gets the value of the numColumns property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumColumns() {
        return numColumns;
    }

    /**
     * Sets the value of the numColumns property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumColumns(Integer value) {
        this.numColumns = value;
    }

    /**
     * Gets the value of the makeColumnsEqualWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isMakeColumnsEqualWidth() {
        if (makeColumnsEqualWidth == null) {
            return false;
        } else {
            return makeColumnsEqualWidth;
        }
    }

    /**
     * Sets the value of the makeColumnsEqualWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMakeColumnsEqualWidth(Boolean value) {
        this.makeColumnsEqualWidth = value;
    }

    /**
     * Gets the value of the marginHeight property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMarginHeight() {
        return marginHeight;
    }

    /**
     * Sets the value of the marginHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMarginHeight(Integer value) {
        this.marginHeight = value;
    }

    /**
     * Gets the value of the marginWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMarginWidth() {
        return marginWidth;
    }

    /**
     * Sets the value of the marginWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMarginWidth(Integer value) {
        this.marginWidth = value;
    }

    /**
     * Gets the value of the marginTop property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMarginTop() {
        return marginTop;
    }

    /**
     * Sets the value of the marginTop property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMarginTop(Integer value) {
        this.marginTop = value;
    }

    /**
     * Gets the value of the marginBottom property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMarginBottom() {
        return marginBottom;
    }

    /**
     * Sets the value of the marginBottom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMarginBottom(Integer value) {
        this.marginBottom = value;
    }

    /**
     * Gets the value of the marginLeft property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMarginLeft() {
        return marginLeft;
    }

    /**
     * Sets the value of the marginLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMarginLeft(Integer value) {
        this.marginLeft = value;
    }

    /**
     * Gets the value of the marginRight property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMarginRight() {
        return marginRight;
    }

    /**
     * Sets the value of the marginRight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMarginRight(Integer value) {
        this.marginRight = value;
    }

    /**
     * Gets the value of the horizontalSpacing property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHorizontalSpacing() {
        return horizontalSpacing;
    }

    /**
     * Sets the value of the horizontalSpacing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHorizontalSpacing(Integer value) {
        this.horizontalSpacing = value;
    }

    /**
     * Gets the value of the verticalSpacing property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVerticalSpacing() {
        return verticalSpacing;
    }

    /**
     * Sets the value of the verticalSpacing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVerticalSpacing(Integer value) {
        this.verticalSpacing = value;
    }

}
