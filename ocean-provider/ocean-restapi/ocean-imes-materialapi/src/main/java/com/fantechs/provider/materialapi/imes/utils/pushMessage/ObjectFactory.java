
package com.fantechs.provider.materialapi.imes.utils.pushMessage;

import com.fantechs.common.base.general.dto.restapi.*;

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

    private final static QName _SendMessageResponse_QNAME = new QName("http://ws.leige.com/", "sendMessageResponse");
    private final static QName _SendMessage_QNAME = new QName("http://ws.leige.com/", "sendMessage");
    private final static QName _SendMpnewsMessage_QNAME = new QName("http://ws.leige.com/", "sendMpnewsMessage");
    private final static QName _SendMpnewsMessageResponse_QNAME = new QName("http://ws.leige.com/", "sendMpnewsMessageResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: testClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendMpnewsMessageResponse }
     * 
     */
    public SendMpnewsMessageResponse createSendMpnewsMessageResponse() {
        return new SendMpnewsMessageResponse();
    }

    /**
     * Create an instance of {@link SendMpnewsMessage }
     * 
     */
    public SendMpnewsMessage createSendMpnewsMessage() {
        return new SendMpnewsMessage();
    }

    /**
     * Create an instance of {@link SendMessage }
     * 
     */
    public SendMessage createSendMessage() {
        return new SendMessage();
    }

    /**
     * Create an instance of {@link SendMessageResponse }
     * 
     */
    public SendMessageResponse createSendMessageResponse() {
        return new SendMessageResponse();
    }

    /**
     * Create an instance of {@link Mpnewsmsginput }
     * 
     */
    public Mpnewsmsginput createMpnewsmsginput() {
        return new Mpnewsmsginput();
    }

    /**
     * Create an instance of {@link Textmsginput }
     * 
     */
    public Textmsginput createTextmsginput() {
        return new Textmsginput();
    }

    /**
     * Create an instance of {@link Resbase }
     * 
     */
    public Resbase createResbase() {
        return new Resbase();
    }

    /**
     * Create an instance of {@link FontText }
     * 
     */
    public FontText createFontText() {
        return new FontText();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.leige.com/", name = "sendMessageResponse")
    public JAXBElement<SendMessageResponse> createSendMessageResponse(SendMessageResponse value) {
        return new JAXBElement<SendMessageResponse>(_SendMessageResponse_QNAME, SendMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.leige.com/", name = "sendMessage")
    public JAXBElement<SendMessage> createSendMessage(SendMessage value) {
        return new JAXBElement<SendMessage>(_SendMessage_QNAME, SendMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMpnewsMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.leige.com/", name = "sendMpnewsMessage")
    public JAXBElement<SendMpnewsMessage> createSendMpnewsMessage(SendMpnewsMessage value) {
        return new JAXBElement<SendMpnewsMessage>(_SendMpnewsMessage_QNAME, SendMpnewsMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMpnewsMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.leige.com/", name = "sendMpnewsMessageResponse")
    public JAXBElement<SendMpnewsMessageResponse> createSendMpnewsMessageResponse(SendMpnewsMessageResponse value) {
        return new JAXBElement<SendMpnewsMessageResponse>(_SendMpnewsMessageResponse_QNAME, SendMpnewsMessageResponse.class, null, value);
    }

}
