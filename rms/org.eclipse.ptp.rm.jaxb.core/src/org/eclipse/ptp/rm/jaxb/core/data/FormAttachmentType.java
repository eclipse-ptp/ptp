//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.19 at 11:21:28 AM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for form-attachment-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="form-attachment-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="alignment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="denominator" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="numerator" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "form-attachment-type")
public class FormAttachmentType {

	@XmlAttribute
	protected String alignment;
	@XmlAttribute
	protected Integer denominator;
	@XmlAttribute
	protected Integer numerator;
	@XmlAttribute
	protected Integer offset;

	/**
	 * Gets the value of the alignment property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAlignment() {
		return alignment;
	}

	/**
	 * Gets the value of the denominator property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getDenominator() {
		return denominator;
	}

	/**
	 * Gets the value of the numerator property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getNumerator() {
		return numerator;
	}

	/**
	 * Gets the value of the offset property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * Sets the value of the alignment property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAlignment(String value) {
		this.alignment = value;
	}

	/**
	 * Sets the value of the denominator property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setDenominator(Integer value) {
		this.denominator = value;
	}

	/**
	 * Sets the value of the numerator property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setNumerator(Integer value) {
		this.numerator = value;
	}

	/**
	 * Sets the value of the offset property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setOffset(Integer value) {
		this.offset = value;
	}

}
