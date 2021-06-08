
package com.fantechs.provider.materialapi.imes.utils.customerApi;

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
@WebServiceClient(name = "SI_MES_CUSTOMER_QUERY_OutService", targetNamespace = "http://leisai.com/None_ECC", wsdlLocation = "http://sappodev.leisai.com:50000/dir/wsdl?p=ic/086bb426eb9032eeb93f7ea0aa39db55")
public class SIMESCUSTOMERQUERYOutService
    extends Service
{

    private final static URL SIMESCUSTOMERQUERYOUTSERVICE_WSDL_LOCATION;
    private final static WebServiceException SIMESCUSTOMERQUERYOUTSERVICE_EXCEPTION;
    private final static QName SIMESCUSTOMERQUERYOUTSERVICE_QNAME = new QName("http://leisai.com/None_ECC", "SI_MES_CUSTOMER_QUERY_OutService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://sappodev.leisai.com:50000/dir/wsdl?p=ic/086bb426eb9032eeb93f7ea0aa39db55");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SIMESCUSTOMERQUERYOUTSERVICE_WSDL_LOCATION = url;
        SIMESCUSTOMERQUERYOUTSERVICE_EXCEPTION = e;
    }

    public SIMESCUSTOMERQUERYOutService() {
        super(__getWsdlLocation(), SIMESCUSTOMERQUERYOUTSERVICE_QNAME);
    }

    public SIMESCUSTOMERQUERYOutService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SIMESCUSTOMERQUERYOUTSERVICE_QNAME, features);
    }

    public SIMESCUSTOMERQUERYOutService(URL wsdlLocation) {
        super(wsdlLocation, SIMESCUSTOMERQUERYOUTSERVICE_QNAME);
    }

    public SIMESCUSTOMERQUERYOutService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SIMESCUSTOMERQUERYOUTSERVICE_QNAME, features);
    }

    public SIMESCUSTOMERQUERYOutService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIMESCUSTOMERQUERYOutService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SIMESCUSTOMERQUERYOut
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIMESCUSTOMERQUERYOut getHTTPPort() {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTP_Port"), SIMESCUSTOMERQUERYOut.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIMESCUSTOMERQUERYOut
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIMESCUSTOMERQUERYOut getHTTPPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTP_Port"), SIMESCUSTOMERQUERYOut.class, features);
    }

    /**
     * 
     * @return
     *     returns SIMESCUSTOMERQUERYOut
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIMESCUSTOMERQUERYOut getHTTPSPort() {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTPS_Port"), SIMESCUSTOMERQUERYOut.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIMESCUSTOMERQUERYOut
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIMESCUSTOMERQUERYOut getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTPS_Port"), SIMESCUSTOMERQUERYOut.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SIMESCUSTOMERQUERYOUTSERVICE_EXCEPTION!= null) {
            throw SIMESCUSTOMERQUERYOUTSERVICE_EXCEPTION;
        }
        return SIMESCUSTOMERQUERYOUTSERVICE_WSDL_LOCATION;
    }

}
