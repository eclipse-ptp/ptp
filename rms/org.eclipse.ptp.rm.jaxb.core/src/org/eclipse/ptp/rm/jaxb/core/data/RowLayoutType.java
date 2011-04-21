//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.21 at 02:35:28 PM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for row-layout-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="row-layout-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="center" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="fill" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="justify" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="marginBottom" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginHeight" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginLeft" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginRight" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginTop" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="marginWidth" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="pack" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="spacing" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "row-layout-type")
public class RowLayoutType {

	@XmlAttribute
	protected Boolean center;
	@XmlAttribute
	protected Boolean fill;
	@XmlAttribute
	protected Boolean justify;
	@XmlAttribute
	protected Integer marginBottom;
	@XmlAttribute
	protected Integer marginHeight;
	@XmlAttribute
	protected Integer marginLeft;
	@XmlAttribute
	protected Integer marginRight;
	@XmlAttribute
	protected Integer marginTop;
	@XmlAttribute
	protected Integer marginWidth;
	@XmlAttribute
	protected Boolean pack;
	@XmlAttribute
	protected Integer spacing;

	/**
	 * Gets the value of the marginBottom property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMarginBottom() {
		return marginBottom;
	}

	/**
	 * Gets the value of the marginHeight property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMarginHeight() {
		return marginHeight;
	}

	/**
	 * Gets the value of the marginLeft property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMarginLeft() {
		return marginLeft;
	}

	/**
	 * Gets the value of the marginRight property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMarginRight() {
		return marginRight;
	}

	/**
	 * Gets the value of the marginTop property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMarginTop() {
		return marginTop;
	}

	/**
	 * Gets the value of the marginWidth property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMarginWidth() {
		return marginWidth;
	}

	/**
	 * Gets the value of the spacing property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getSpacing() {
		return spacing;
	}

	/**
	 * Gets the value of the center property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isCenter() {
		if (center == null) {
			return false;
		} else {
			return center;
		}
	}

	/**
	 * Gets the value of the fill property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isFill() {
		if (fill == null) {
			return false;
		} else {
			return fill;
		}
	}

	/**
	 * Gets the value of the justify property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isJustify() {
		if (justify == null) {
			return false;
		} else {
			return justify;
		}
	}

	/**
	 * Gets the value of the pack property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isPack() {
		if (pack == null) {
			return false;
		} else {
			return pack;
		}
	}

	/**
	 * Sets the value of the center property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setCenter(Boolean value) {
		this.center = value;
	}

	/**
	 * Sets the value of the fill property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setFill(Boolean value) {
		this.fill = value;
	}

	/**
	 * Sets the value of the justify property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setJustify(Boolean value) {
		this.justify = value;
	}

	/**
	 * Sets the value of the marginBottom property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMarginBottom(Integer value) {
		this.marginBottom = value;
	}

	/**
	 * Sets the value of the marginHeight property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMarginHeight(Integer value) {
		this.marginHeight = value;
	}

	/**
	 * Sets the value of the marginLeft property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMarginLeft(Integer value) {
		this.marginLeft = value;
	}

	/**
	 * Sets the value of the marginRight property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMarginRight(Integer value) {
		this.marginRight = value;
	}

	/**
	 * Sets the value of the marginTop property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMarginTop(Integer value) {
		this.marginTop = value;
	}

	/**
	 * Sets the value of the marginWidth property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setMarginWidth(Integer value) {
		this.marginWidth = value;
	}

	/**
	 * Sets the value of the pack property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setPack(Boolean value) {
		this.pack = value;
	}

	/**
	 * Sets the value of the spacing property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setSpacing(Integer value) {
		this.spacing = value;
	}

}
