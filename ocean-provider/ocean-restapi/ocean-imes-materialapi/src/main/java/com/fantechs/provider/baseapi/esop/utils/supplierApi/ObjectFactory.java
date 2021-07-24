
package com.fantechs.provider.baseapi.esop.utils.supplierApi;

import com.fantechs.common.base.general.dto.restapi.DTMESSUPPLIER;
import com.fantechs.common.base.general.dto.restapi.DTMESSUPPLIERQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESSUPPLIERQUERYRES;

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

    private final static QName _MTMESSUPPLIERQUERYREQ_QNAME = new QName("http://leisai.com/ECC", "MT_MES_SUPPLIER_QUERY_REQ");
    private final static QName _MTMESSUPPLIERQUERYRES_QNAME = new QName("http://leisai.com/ECC", "MT_MES_SUPPLIER_QUERY_RES");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTMESSUPPLIERQUERYRES }
     * 
     */
    public DTMESSUPPLIERQUERYRES createDTMESSUPPLIERQUERYRES() {
        return new DTMESSUPPLIERQUERYRES();
    }

    /**
     * Create an instance of {@link DTMESSUPPLIERQUERYREQ }
     * 
     */
    public DTMESSUPPLIERQUERYREQ createDTMESSUPPLIERQUERYREQ() {
        return new DTMESSUPPLIERQUERYREQ();
    }

    /**
     * Create an instance of {@link DTMESSUPPLIER }
     * 
     */
    public DTMESSUPPLIER createDTMESSUPPLIER() {
        return new DTMESSUPPLIER();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESSUPPLIERQUERYREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_SUPPLIER_QUERY_REQ")
    public JAXBElement<DTMESSUPPLIERQUERYREQ> createMTMESSUPPLIERQUERYREQ(DTMESSUPPLIERQUERYREQ value) {
        return new JAXBElement<DTMESSUPPLIERQUERYREQ>(_MTMESSUPPLIERQUERYREQ_QNAME, DTMESSUPPLIERQUERYREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESSUPPLIERQUERYRES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_SUPPLIER_QUERY_RES")
    public JAXBElement<DTMESSUPPLIERQUERYRES> createMTMESSUPPLIERQUERYRES(DTMESSUPPLIERQUERYRES value) {
        return new JAXBElement<DTMESSUPPLIERQUERYRES>(_MTMESSUPPLIERQUERYRES_QNAME, DTMESSUPPLIERQUERYRES.class, null, value);
    }

}
