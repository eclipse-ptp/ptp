//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.05 at 08:09:06 AM EST 
//


package org.eclipse.ptp.rm.jaxb.core.data.lml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * A pattern element consists of include and exclude
 * 				tags. Values of the corresponding column
 * 				can be checked by the defined regular expressions. They must pass the
 * 				regexp-checks in
 * 				the following way: Go through all include/exclude tags. A value must be
 * 				valid against the
 * 				include-regexps and must not match with the exclude-regexps. Therefore the order of
 * 				tags
 * 				is important.
 * 			
 * 
 * <p>Java class for pattern_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pattern_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="include" type="{http://eclipse.org/ptp/lml}pattern_match_type" minOccurs="0"/>
 *         &lt;element name="exclude" type="{http://eclipse.org/ptp/lml}pattern_match_type" minOccurs="0"/>
 *         &lt;element name="select" type="{http://eclipse.org/ptp/lml}select_type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pattern_type", propOrder = {
    "includeAndExcludeAndSelect"
})
public class PatternType {

    @XmlElementRefs({
        @XmlElementRef(name = "include", type = JAXBElement.class),
        @XmlElementRef(name = "select", type = JAXBElement.class),
        @XmlElementRef(name = "exclude", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> includeAndExcludeAndSelect;

    /**
     * Gets the value of the includeAndExcludeAndSelect property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeAndExcludeAndSelect property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeAndExcludeAndSelect().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link PatternMatchType }{@code >}
     * {@link JAXBElement }{@code <}{@link SelectType }{@code >}
     * {@link JAXBElement }{@code <}{@link PatternMatchType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getIncludeAndExcludeAndSelect() {
        if (includeAndExcludeAndSelect == null) {
            includeAndExcludeAndSelect = new ArrayList<JAXBElement<?>>();
        }
        return this.includeAndExcludeAndSelect;
    }

}
