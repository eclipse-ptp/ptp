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
 * <p>Java class for nodedisplayelement5 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nodedisplayelement5">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}nodedisplayelement">
 *       &lt;sequence>
 *         &lt;element name="el6" type="{http://eclipse.org/ptp/lml}nodedisplayelement6" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodedisplayelement5", propOrder = {
    "el6"
})
public class Nodedisplayelement5
    extends Nodedisplayelement
{

    protected List<Nodedisplayelement6> el6;

    /**
     * Gets the value of the el6 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the el6 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEl6().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nodedisplayelement6 }
     * 
     * 
     */
    public List<Nodedisplayelement6> getEl6() {
        if (el6 == null) {
            el6 = new ArrayList<Nodedisplayelement6>();
        }
        return this.el6;
    }

}
