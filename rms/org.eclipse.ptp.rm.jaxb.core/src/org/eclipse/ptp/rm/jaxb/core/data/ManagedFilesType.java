//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.19 at 11:21:28 AM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for managed-files-type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="managed-files-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="file-staging-location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="file" type="{http://org.eclipse.ptp/rm}managed-file-type" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "managed-files-type", propOrder = { "fileStagingLocation", "file" })
public class ManagedFilesType {

	@XmlElement(name = "file-staging-location")
	protected String fileStagingLocation;
	@XmlElement(required = true)
	protected List<ManagedFileType> file;

	/**
	 * Gets the value of the file property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the file property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFile().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ManagedFileType }
	 * 
	 * 
	 */
	public List<ManagedFileType> getFile() {
		if (file == null) {
			file = new ArrayList<ManagedFileType>();
		}
		return this.file;
	}

	/**
	 * Gets the value of the fileStagingLocation property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFileStagingLocation() {
		return fileStagingLocation;
	}

	/**
	 * Sets the value of the fileStagingLocation property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFileStagingLocation(String value) {
		this.fileStagingLocation = value;
	}

}
