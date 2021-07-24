
package com.fantechs.provider.materialapi.imes.utils.reportWorkApi;

import com.fantechs.common.base.general.dto.restapi.DTMESWORKORDERREPORTSAVEREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESWORKORDERREPORTSAVERES;

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

    private final static QName _MTMESWORKORDERREPORTSAVEREQ_QNAME = new QName("http://leisai.com/ECC", "MT_MES_WORKORDER_REPORT_SAVE_REQ");
    private final static QName _MTMESWORKORDERREPORTSAVERES_QNAME = new QName("http://leisai.com/ECC", "MT_MES_WORKORDER_REPORT_SAVE_RES");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTMESWORKORDERREPORTSAVERES }
     * 
     */
    public DTMESWORKORDERREPORTSAVERES createDTMESWORKORDERREPORTSAVERES() {
        return new DTMESWORKORDERREPORTSAVERES();
    }

    /**
     * Create an instance of {@link DTMESWORKORDERREPORTSAVEREQ }
     * 
     */
    public DTMESWORKORDERREPORTSAVEREQ createDTMESWORKORDERREPORTSAVEREQ() {
        return new DTMESWORKORDERREPORTSAVEREQ();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESWORKORDERREPORTSAVEREQ }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_WORKORDER_REPORT_SAVE_REQ")
    public JAXBElement<DTMESWORKORDERREPORTSAVEREQ> createMTMESWORKORDERREPORTSAVEREQ(DTMESWORKORDERREPORTSAVEREQ value) {
        return new JAXBElement<DTMESWORKORDERREPORTSAVEREQ>(_MTMESWORKORDERREPORTSAVEREQ_QNAME, DTMESWORKORDERREPORTSAVEREQ.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTMESWORKORDERREPORTSAVERES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leisai.com/ECC", name = "MT_MES_WORKORDER_REPORT_SAVE_RES")
    public JAXBElement<DTMESWORKORDERREPORTSAVERES> createMTMESWORKORDERREPORTSAVERES(DTMESWORKORDERREPORTSAVERES value) {
        return new JAXBElement<DTMESWORKORDERREPORTSAVERES>(_MTMESWORKORDERREPORTSAVERES_QNAME, DTMESWORKORDERREPORTSAVERES.class, null, value);
    }

}
