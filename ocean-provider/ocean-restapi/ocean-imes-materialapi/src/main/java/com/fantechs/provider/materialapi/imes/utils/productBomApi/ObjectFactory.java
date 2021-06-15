
package com.fantechs.provider.materialapi.imes.utils.productBomApi;

import com.fantechs.common.base.general.dto.restapi.DTMESBOM;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYRES;

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

    private final static QName _MTMESBOMQUERYREQ_QNAME = new QName("http://leisai.com/ECC", "MT_MES_BOM_QUERY_REQ");
    private final static QName _MTMESBOMQUERYRES_QNAME = new QName("http://leisai.com/ECC", "MT_MES_BOM_QUERY_RES");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTMESBOMQUERYRES }
     * 
     */
    public DTMESBOMQUERYRES createDTMESBOMQUERYRES() {
        return new DTMESBOMQUERYRES();
    }

    /**
     * Create an instance of {@link DTMESBOMQUERYREQ }
     * 
     */
    public DTMESBOMQUERYREQ createDTMESBOMQUERYREQ() {
        return new DTMESBOMQUERYREQ();
    }

    /**
     * Create an instance of {@link DTMESBOM }
     * 
     */
    public DTMESBOM createDTMESBOM() {
        return new DTMESBOM();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESBOMQUERYREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_BOM_QUERY_REQ")
    public JAXBElement<DTMESBOMQUERYREQ> createMTMESBOMQUERYREQ(DTMESBOMQUERYREQ value) {
        return new JAXBElement<DTMESBOMQUERYREQ>(_MTMESBOMQUERYREQ_QNAME, DTMESBOMQUERYREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESBOMQUERYRES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_BOM_QUERY_RES")
    public JAXBElement<DTMESBOMQUERYRES> createMTMESBOMQUERYRES(DTMESBOMQUERYRES value) {
        return new JAXBElement<DTMESBOMQUERYRES>(_MTMESBOMQUERYRES_QNAME, DTMESBOMQUERYRES.class, null, value);
    }

}
