
package com.fantechs.provider.materialapi.imes.utils.customerApi;

import com.fantechs.common.base.general.dto.restapi.DTMESCUSTOMERQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESCUSTOMERQUERYRES;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SI_MES_CUSTOMER_QUERY_Out", targetNamespace = "http://leisai.com/None_ECC")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SIMESCUSTOMERQUERYOut {


    /**
     * 
     * @param mtMESCUSTOMERQUERYREQ
     * @return
     *     returns testClient.DTMESCUSTOMERQUERYRES
     */
    @WebMethod(operationName = "SI_MES_CUSTOMER_QUERY_Out", action = "http://sap.com/xi/WebService/soap1.1")
    @WebResult(name = "MT_MES_CUSTOMER_QUERY_RES", targetNamespace = "http://leisai.com/ECC", partName = "MT_MES_CUSTOMER_QUERY_RES")
    public DTMESCUSTOMERQUERYRES siMESCUSTOMERQUERYOut(
        @WebParam(name = "MT_MES_CUSTOMER_QUERY_REQ", targetNamespace = "http://leisai.com/ECC", partName = "MT_MES_CUSTOMER_QUERY_REQ")
                DTMESCUSTOMERQUERYREQ mtMESCUSTOMERQUERYREQ);

}