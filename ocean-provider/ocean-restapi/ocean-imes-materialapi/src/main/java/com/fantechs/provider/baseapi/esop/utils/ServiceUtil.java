package com.fantechs.provider.baseapi.esop.utils;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;


public class ServiceUtil {

    /**
     * 获取服务方法
     *
     * @param wsUrl
     * @param nameSpaceURI
     * @param localPart
     * @return
     */
    public static Service getService(String wsUrl, String nameSpaceURI, String localPart) {
        URL url = null;//webservice服务端给出的URL
        try {
            url = new URL(wsUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QName qName = new QName(nameSpaceURI, localPart);//第一个参数是wsdl的targetNamespace，第二个是对应的name
        return Service.create(url, qName);
    }

}
