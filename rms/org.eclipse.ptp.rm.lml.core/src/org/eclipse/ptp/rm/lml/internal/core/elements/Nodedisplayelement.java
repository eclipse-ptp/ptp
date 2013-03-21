//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 09:26:24 AM EDT 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Contains all attributes that are equal at all levels
 * 			
 * 
 * <p>Java class for nodedisplayelement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nodedisplayelement">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}element_type">
 *       &lt;sequence>
 *         &lt;element name="img" type="{http://eclipse.org/ptp/lml}picture_type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="rows" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *       &lt;attribute name="cols" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="8" />
 *       &lt;attribute name="hgap" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="1" />
 *       &lt;attribute name="vgap" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="1" />
 *       &lt;attribute name="fontsize" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="10" />
 *       &lt;attribute name="border" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="1" />
 *       &lt;attribute name="fontfamily" type="{http://www.w3.org/2001/XMLSchema}string" default="Monospaced" />
 *       &lt;attribute name="showtitle" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="titlebackground" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#EFEFEF" />
 *       &lt;attribute name="background" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#FFF" />
 *       &lt;attribute name="maxlevel" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="mouseborder" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="2" />
 *       &lt;attribute name="transparent" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="bordercolor" type="{http://eclipse.org/ptp/lml}hexcolor_type" default="#000" />
 *       &lt;attribute name="showfulltitle" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="highestrowfirst" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="highestcolfirst" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodedisplayelement", propOrder = {
    "img"
})
@XmlSeeAlso({
    Nodedisplayelement2 .class,
    Nodedisplayelement3 .class,
    Nodedisplayelement0 .class,
    Nodedisplayelement1 .class,
    Nodedisplayelement8 .class,
    Nodedisplayelement9 .class,
    Nodedisplayelement6 .class,
    Nodedisplayelement7 .class,
    Nodedisplayelement4 .class,
    Nodedisplayelement5 .class
})
public class Nodedisplayelement
    extends ElementType
{

    protected List<PictureType> img;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger rows;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cols;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger hgap;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger vgap;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger fontsize;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger border;
    @XmlAttribute
    protected String fontfamily;
    @XmlAttribute
    protected Boolean showtitle;
    @XmlAttribute
    protected String titlebackground;
    @XmlAttribute
    protected String background;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger maxlevel;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger mouseborder;
    @XmlAttribute
    protected Boolean transparent;
    @XmlAttribute
    protected String bordercolor;
    @XmlAttribute
    protected Boolean showfulltitle;
    @XmlAttribute
    protected Boolean highestrowfirst;
    @XmlAttribute
    protected Boolean highestcolfirst;

    /**
     * Gets the value of the img property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the img property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PictureType }
     * 
     * 
     */
    public List<PictureType> getImg() {
        if (img == null) {
            img = new ArrayList<PictureType>();
        }
        return this.img;
    }

    /**
     * Gets the value of the rows property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRows() {
        if (rows == null) {
            return new BigInteger("0");
        } else {
            return rows;
        }
    }

    /**
     * Sets the value of the rows property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRows(BigInteger value) {
        this.rows = value;
    }

    /**
     * Gets the value of the cols property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCols() {
        if (cols == null) {
            return new BigInteger("8");
        } else {
            return cols;
        }
    }

    /**
     * Sets the value of the cols property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCols(BigInteger value) {
        this.cols = value;
    }

    /**
     * Gets the value of the hgap property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHgap() {
        if (hgap == null) {
            return new BigInteger("1");
        } else {
            return hgap;
        }
    }

    /**
     * Sets the value of the hgap property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHgap(BigInteger value) {
        this.hgap = value;
    }

    /**
     * Gets the value of the vgap property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVgap() {
        if (vgap == null) {
            return new BigInteger("1");
        } else {
            return vgap;
        }
    }

    /**
     * Sets the value of the vgap property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVgap(BigInteger value) {
        this.vgap = value;
    }

    /**
     * Gets the value of the fontsize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFontsize() {
        if (fontsize == null) {
            return new BigInteger("10");
        } else {
            return fontsize;
        }
    }

    /**
     * Sets the value of the fontsize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFontsize(BigInteger value) {
        this.fontsize = value;
    }

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
            return new BigInteger("1");
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
     * Gets the value of the fontfamily property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFontfamily() {
        if (fontfamily == null) {
            return "Monospaced";
        } else {
            return fontfamily;
        }
    }

    /**
     * Sets the value of the fontfamily property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFontfamily(String value) {
        this.fontfamily = value;
    }

    /**
     * Gets the value of the showtitle property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowtitle() {
        if (showtitle == null) {
            return false;
        } else {
            return showtitle;
        }
    }

    /**
     * Sets the value of the showtitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowtitle(Boolean value) {
        this.showtitle = value;
    }

    /**
     * Gets the value of the titlebackground property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitlebackground() {
        if (titlebackground == null) {
            return "#EFEFEF";
        } else {
            return titlebackground;
        }
    }

    /**
     * Sets the value of the titlebackground property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitlebackground(String value) {
        this.titlebackground = value;
    }

    /**
     * Gets the value of the background property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackground() {
        if (background == null) {
            return "#FFF";
        } else {
            return background;
        }
    }

    /**
     * Sets the value of the background property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackground(String value) {
        this.background = value;
    }

    /**
     * Gets the value of the maxlevel property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxlevel() {
        return maxlevel;
    }

    /**
     * Sets the value of the maxlevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxlevel(BigInteger value) {
        this.maxlevel = value;
    }

    /**
     * Gets the value of the mouseborder property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMouseborder() {
        if (mouseborder == null) {
            return new BigInteger("2");
        } else {
            return mouseborder;
        }
    }

    /**
     * Sets the value of the mouseborder property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMouseborder(BigInteger value) {
        this.mouseborder = value;
    }

    /**
     * Gets the value of the transparent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTransparent() {
        if (transparent == null) {
            return true;
        } else {
            return transparent;
        }
    }

    /**
     * Sets the value of the transparent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransparent(Boolean value) {
        this.transparent = value;
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
     * Gets the value of the showfulltitle property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowfulltitle() {
        if (showfulltitle == null) {
            return true;
        } else {
            return showfulltitle;
        }
    }

    /**
     * Sets the value of the showfulltitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowfulltitle(Boolean value) {
        this.showfulltitle = value;
    }

    /**
     * Gets the value of the highestrowfirst property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHighestrowfirst() {
        if (highestrowfirst == null) {
            return false;
        } else {
            return highestrowfirst;
        }
    }

    /**
     * Sets the value of the highestrowfirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHighestrowfirst(Boolean value) {
        this.highestrowfirst = value;
    }

    /**
     * Gets the value of the highestcolfirst property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHighestcolfirst() {
        if (highestcolfirst == null) {
            return false;
        } else {
            return highestcolfirst;
        }
    }

    /**
     * Sets the value of the highestcolfirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHighestcolfirst(Boolean value) {
        this.highestcolfirst = value;
    }

}
