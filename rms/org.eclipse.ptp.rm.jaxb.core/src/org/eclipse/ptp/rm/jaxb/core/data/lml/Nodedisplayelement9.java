//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.05 at 08:09:06 AM EST 
//


package org.eclipse.ptp.rm.jaxb.core.data.lml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nodedisplayelement9 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nodedisplayelement9">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}nodedisplayelement">
 *       &lt;sequence>
 *         &lt;element name="el10" type="{http://eclipse.org/ptp/lml}nodedisplayelement" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodedisplayelement9", propOrder = {
    "el10"
})
public class Nodedisplayelement9
    extends Nodedisplayelement
{

    protected List<Nodedisplayelement> el10;

    /**
     * Gets the value of the el10 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the el10 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEl10().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nodedisplayelement }
     * 
     * 
     */
    public List<Nodedisplayelement> getEl10() {
        if (el10 == null) {
            el10 = new ArrayList<Nodedisplayelement>();
        }
        return this.el10;
    }

}
