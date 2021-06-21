
package com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi;

import com.fantechs.common.base.general.dto.restapi.DTMESBADCODE;
import com.fantechs.common.base.general.dto.restapi.DTMESBADCODEQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBADCODEQUERYRES;

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

    private final static QName _MTMESBADCODEQUERYREQ_QNAME = new QName("http://leisai.com/ECC", "MT_MES_BADCODE_QUERY_REQ");
    private final static QName _MTMESBADCODEQUERYRES_QNAME = new QName("http://leisai.com/ECC", "MT_MES_BADCODE_QUERY_RES");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTMESBADCODEQUERYRES }
     * 
     */
    public DTMESBADCODEQUERYRES createDTMESBADCODEQUERYRES() {
        return new DTMESBADCODEQUERYRES();
    }

    /**
     * Create an instance of {@link DTMESBADCODEQUERYREQ }
     * 
     */
    public DTMESBADCODEQUERYREQ createDTMESBADCODEQUERYREQ() {
        return new DTMESBADCODEQUERYREQ();
    }

    /**
     * Create an instance of {@link DTMESBADCODE }
     * 
     */
    public DTMESBADCODE createDTMESBADCODE() {
        return new DTMESBADCODE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESBADCODEQUERYREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_BADCODE_QUERY_REQ")
    public JAXBElement<DTMESBADCODEQUERYREQ> createMTMESBADCODEQUERYREQ(DTMESBADCODEQUERYREQ value) {
        return new JAXBElement<DTMESBADCODEQUERYREQ>(_MTMESBADCODEQUERYREQ_QNAME, DTMESBADCODEQUERYREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESBADCODEQUERYRES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_BADCODE_QUERY_RES")
    public JAXBElement<DTMESBADCODEQUERYRES> createMTMESBADCODEQUERYRES(DTMESBADCODEQUERYRES value) {
        return new JAXBElement<DTMESBADCODEQUERYRES>(_MTMESBADCODEQUERYRES_QNAME, DTMESBADCODEQUERYRES.class, null, value);
    }

}
