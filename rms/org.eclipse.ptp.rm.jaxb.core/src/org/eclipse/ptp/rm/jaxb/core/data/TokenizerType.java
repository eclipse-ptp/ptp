//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.02.12 at 06:02:35 PM EST 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tokenizer-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tokenizer-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="target" type="{org.eclipse.ptp}target-type" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *         &lt;element name="exit-on" type="{org.eclipse.ptp}regex-type" minOccurs="0"/>
 *         &lt;element name="exit-after" type="{org.eclipse.ptp}regex-type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="delim" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="includeDelim" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="maxMatchLen" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="all" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="save" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="applyToAll" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tokenizer-type", propOrder = {
    "type",
    "target",
    "exitOn",
    "exitAfter"
})
public class TokenizerType {

    protected String type;
    protected List<TargetType> target;
    @XmlElement(name = "exit-on")
    protected RegexType exitOn;
    @XmlElement(name = "exit-after")
    protected RegexType exitAfter;
    @XmlAttribute(name = "delim")
    protected String delim;
    @XmlAttribute(name = "includeDelim")
    protected Boolean includeDelim;
    @XmlAttribute(name = "maxMatchLen")
    protected Integer maxMatchLen;
    @XmlAttribute(name = "all")
    protected Boolean all;
    @XmlAttribute(name = "save")
    protected Integer save;
    @XmlAttribute(name = "applyToAll")
    protected Boolean applyToAll;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the target property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTarget().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TargetType }
     * 
     * 
     */
    public List<TargetType> getTarget() {
        if (target == null) {
            target = new ArrayList<TargetType>();
        }
        return this.target;
    }

    /**
     * Gets the value of the exitOn property.
     * 
     * @return
     *     possible object is
     *     {@link RegexType }
     *     
     */
    public RegexType getExitOn() {
        return exitOn;
    }

    /**
     * Sets the value of the exitOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegexType }
     *     
     */
    public void setExitOn(RegexType value) {
        this.exitOn = value;
    }

    /**
     * Gets the value of the exitAfter property.
     * 
     * @return
     *     possible object is
     *     {@link RegexType }
     *     
     */
    public RegexType getExitAfter() {
        return exitAfter;
    }

    /**
     * Sets the value of the exitAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegexType }
     *     
     */
    public void setExitAfter(RegexType value) {
        this.exitAfter = value;
    }

    /**
     * Gets the value of the delim property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelim() {
        return delim;
    }

    /**
     * Sets the value of the delim property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelim(String value) {
        this.delim = value;
    }

    /**
     * Gets the value of the includeDelim property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIncludeDelim() {
        if (includeDelim == null) {
            return false;
        } else {
            return includeDelim;
        }
    }

    /**
     * Sets the value of the includeDelim property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeDelim(Boolean value) {
        this.includeDelim = value;
    }

    /**
     * Gets the value of the maxMatchLen property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getMaxMatchLen() {
        if (maxMatchLen == null) {
            return  0;
        } else {
            return maxMatchLen;
        }
    }

    /**
     * Sets the value of the maxMatchLen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxMatchLen(Integer value) {
        this.maxMatchLen = value;
    }

    /**
     * Gets the value of the all property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAll() {
        if (all == null) {
            return false;
        } else {
            return all;
        }
    }

    /**
     * Sets the value of the all property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAll(Boolean value) {
        this.all = value;
    }

    /**
     * Gets the value of the save property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getSave() {
        if (save == null) {
            return  0;
        } else {
            return save;
        }
    }

    /**
     * Sets the value of the save property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSave(Integer value) {
        this.save = value;
    }

    /**
     * Gets the value of the applyToAll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isApplyToAll() {
        if (applyToAll == null) {
            return false;
        } else {
            return applyToAll;
        }
    }

    /**
     * Sets the value of the applyToAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setApplyToAll(Boolean value) {
        this.applyToAll = value;
    }

}
