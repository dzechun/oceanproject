
package com.fantechs.provider.materialapi.imes.utils.ProLineApi;

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
@WebServiceClient(name = "SI_MES_PROCESS_QUERY_OutService", targetNamespace = "http://leisai.com/None_ECC", wsdlLocation = "http://sappodev.leisai.com:50000/dir/wsdl?p=ic/25bf425eee47327abc7315b463758e1f")
public class SIMESPROCESSQUERYOutService
    extends Service
{

    private final static URL SIMESPROCESSQUERYOUTSERVICE_WSDL_LOCATION;
    private final static WebServiceException SIMESPROCESSQUERYOUTSERVICE_EXCEPTION;
    private final static QName SIMESPROCESSQUERYOUTSERVICE_QNAME = new QName("http://leisai.com/None_ECC", "SI_MES_PROCESS_QUERY_OutService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://sappodev.leisai.com:50000/dir/wsdl?p=ic/25bf425eee47327abc7315b463758e1f");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SIMESPROCESSQUERYOUTSERVICE_WSDL_LOCATION = url;
        SIMESPROCESSQUERYOUTSERVICE_EXCEPTION = e;
    }

    public SIMESPROCESSQUERYOutService() {
        super(__getWsdlLocation(), SIMESPROCESSQUERYOUTSERVICE_QNAME);
    }

    public SIMESPROCESSQUERYOutService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SIMESPROCESSQUERYOUTSERVICE_QNAME, features);
    }

    public SIMESPROCESSQUERYOutService(URL wsdlLocation) {
        super(wsdlLocation, SIMESPROCESSQUERYOUTSERVICE_QNAME);
    }

    public SIMESPROCESSQUERYOutService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SIMESPROCESSQUERYOUTSERVICE_QNAME, features);
    }

    public SIMESPROCESSQUERYOutService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIMESPROCESSQUERYOutService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SIMESPROCESSQUERYOut
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIMESPROCESSQUERYOut getHTTPPort() {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTP_Port"), SIMESPROCESSQUERYOut.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIMESPROCESSQUERYOut
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIMESPROCESSQUERYOut getHTTPPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTP_Port"), SIMESPROCESSQUERYOut.class, features);
    }

    /**
     * 
     * @return
     *     returns SIMESPROCESSQUERYOut
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIMESPROCESSQUERYOut getHTTPSPort() {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTPS_Port"), SIMESPROCESSQUERYOut.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIMESPROCESSQUERYOut
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIMESPROCESSQUERYOut getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://leisai.com/None_ECC", "HTTPS_Port"), SIMESPROCESSQUERYOut.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SIMESPROCESSQUERYOUTSERVICE_EXCEPTION!= null) {
            throw SIMESPROCESSQUERYOUTSERVICE_EXCEPTION;
        }
        return SIMESPROCESSQUERYOUTSERVICE_WSDL_LOCATION;
    }

}
