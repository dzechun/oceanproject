
package com.fantechs.provider.materialapi.imes.utils.ProLineApi;

import com.fantechs.common.base.general.dto.restapi.DTMESPROCESS;
import com.fantechs.common.base.general.dto.restapi.DTMESPROCESSQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESPROCESSQUERYRES;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the testClient package. 
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

    private final static QName _MTMESPROCESSQUERYREQ_QNAME = new QName("http://leisai.com/ECC", "MT_MES_PROCESS_QUERY_REQ");
    private final static QName _MTMESPROCESSQUERYRES_QNAME = new QName("http://leisai.com/ECC", "MT_MES_PROCESS_QUERY_RES");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTMESPROCESSQUERYREQ }
     * 
     */
    public DTMESPROCESSQUERYREQ createDTMESPROCESSQUERYREQ() {
        return new DTMESPROCESSQUERYREQ();
    }

    /**
     * Create an instance of {@link DTMESPROCESSQUERYRES }
     * 
     */
    public DTMESPROCESSQUERYRES createDTMESPROCESSQUERYRES() {
        return new DTMESPROCESSQUERYRES();
    }

    /**
     * Create an instance of {@link DTMESPROCESS }
     * 
     */
    public DTMESPROCESS createDTMESPROCESS() {
        return new DTMESPROCESS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESPROCESSQUERYREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_PROCESS_QUERY_REQ")
    public JAXBElement<DTMESPROCESSQUERYREQ> createMTMESPROCESSQUERYREQ(DTMESPROCESSQUERYREQ value) {
        return new JAXBElement<DTMESPROCESSQUERYREQ>(_MTMESPROCESSQUERYREQ_QNAME, DTMESPROCESSQUERYREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESPROCESSQUERYRES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_PROCESS_QUERY_RES")
    public JAXBElement<DTMESPROCESSQUERYRES> createMTMESPROCESSQUERYRES(DTMESPROCESSQUERYRES value) {
        return new JAXBElement<DTMESPROCESSQUERYRES>(_MTMESPROCESSQUERYRES_QNAME, DTMESPROCESSQUERYRES.class, null, value);
    }

}
