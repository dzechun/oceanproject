
package com.fantechs.provider.materialapi.imes.utils.materialApi;

import com.fantechs.common.base.general.dto.restapi.DTMESMATERIAL;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIALQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIALQUERYRES;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.com.testClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MTMESMATERIALQUERYRES_QNAME = new QName("http://leisai.com/ECC", "MT_MES_MATERIAL_QUERY_RES");
    private final static QName _MTMESMATERIALQUERYREQ_QNAME = new QName("http://leisai.com/ECC", "MT_MES_MATERIAL_QUERY_REQ");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTMESMATERIALQUERYRES }
     * 
     */
    public DTMESMATERIALQUERYRES createDTMESMATERIALQUERYRES() {
        return new DTMESMATERIALQUERYRES();
    }

    /**
     * Create an instance of {@link DTMESMATERIALQUERYREQ }
     * 
     */
    public DTMESMATERIALQUERYREQ createDTMESMATERIALQUERYREQ() {
        return new DTMESMATERIALQUERYREQ();
    }

    /**
     * Create an instance of {@link DTMESMATERIAL }
     * 
     */
    public DTMESMATERIAL createDTMESMATERIAL() {
        return new DTMESMATERIAL();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESMATERIALQUERYRES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_MATERIAL_QUERY_RES")
    public JAXBElement<DTMESMATERIALQUERYRES> createMTMESMATERIALQUERYRES(DTMESMATERIALQUERYRES value) {
        return new JAXBElement<DTMESMATERIALQUERYRES>(_MTMESMATERIALQUERYRES_QNAME, DTMESMATERIALQUERYRES.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESMATERIALQUERYREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_MATERIAL_QUERY_REQ")
    public JAXBElement<DTMESMATERIALQUERYREQ> createMTMESMATERIALQUERYREQ(DTMESMATERIALQUERYREQ value) {
        return new JAXBElement<DTMESMATERIALQUERYREQ>(_MTMESMATERIALQUERYREQ_QNAME, DTMESMATERIALQUERYREQ.class, null, value);
    }

}
