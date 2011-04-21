//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.21 at 02:35:28 PM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * References (or creates) a string (buffer) as the value of the target field,
 * and appends the entry values to it.
 * 
 * 
 * <p>
 * Java class for append-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="append-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entry" type="{http://org.eclipse.ptp/rm}entry-type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="field" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="separator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "append-type", propOrder = { "entry" })
public class AppendType {

	protected List<EntryType> entry;
	@XmlAttribute
	protected String field;
	@XmlAttribute
	protected String separator;

	/**
	 * Gets the value of the entry property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the entry property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getEntry().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link EntryType }
	 * 
	 * 
	 */
	public List<EntryType> getEntry() {
		if (entry == null) {
			entry = new ArrayList<EntryType>();
		}
		return this.entry;
	}

	/**
	 * Gets the value of the field property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getField() {
		return field;
	}

	/**
	 * Gets the value of the separator property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Sets the value of the field property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setField(String value) {
		this.field = value;
	}

	/**
	 * Sets the value of the separator property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSeparator(String value) {
		this.separator = value;
	}

}
