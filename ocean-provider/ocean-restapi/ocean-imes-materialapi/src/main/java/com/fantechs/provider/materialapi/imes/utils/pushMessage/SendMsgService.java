
package com.fantechs.provider.materialapi.imes.utils.pushMessage;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SendMsgService", targetNamespace = "http://ws.leige.com/", wsdlLocation = "http://192.168.0.217:9091/Broadcast/sendMSG?wsdl")
public class SendMsgService
    extends Service
{

    private final static URL SENDMSGSERVICE_WSDL_LOCATION;
    private final static WebServiceException SENDMSGSERVICE_EXCEPTION;
    private final static QName SENDMSGSERVICE_QNAME = new QName("http://ws.leige.com/", "SendMsgService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.0.217:9091/Broadcast/sendMSG?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SENDMSGSERVICE_WSDL_LOCATION = url;
        SENDMSGSERVICE_EXCEPTION = e;
    }

    public SendMsgService() {
        super(__getWsdlLocation(), SENDMSGSERVICE_QNAME);
    }

    public SendMsgService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SENDMSGSERVICE_QNAME, features);
    }

    public SendMsgService(URL wsdlLocation) {
        super(wsdlLocation, SENDMSGSERVICE_QNAME);
    }

    public SendMsgService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SENDMSGSERVICE_QNAME, features);
    }

    public SendMsgService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SendMsgService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SendMsg
     */
    @WebEndpoint(name = "SendMsgPort")
    public SendMsg getSendMsgPort() {
        return super.getPort(new QName("http://ws.leige.com/", "SendMsgPort"), SendMsg.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SendMsg
     */
    @WebEndpoint(name = "SendMsgPort")
    public SendMsg getSendMsgPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.leige.com/", "SendMsgPort"), SendMsg.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SENDMSGSERVICE_EXCEPTION!= null) {
            throw SENDMSGSERVICE_EXCEPTION;
        }
        return SENDMSGSERVICE_WSDL_LOCATION;
    }

}
