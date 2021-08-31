package com.fantechs.provider.chinafiveringapi.api.service.impl;

import com.fantechs.provider.chinafiveringapi.api.service.ExportDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExportDataServiceImpl implements ExportDataService {

    // 接口地址
    private final String address = "http://mattest.cwcec.com/LocWebServices/WebService1.asmx";

//    @Resource
//    private WebserviceUtil webserviceUtil;

    @Override
    public String writeDeliveryDetails(String jsonVoiceArray, String projectID) throws Exception{
//        WebService1HttpGet webService1HttpGet=null;
//        // 代理工厂
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//        // 设置代理地址
//        jaxWsProxyFactoryBean.setAddress(address);
//        // 设置接口类型
//        jaxWsProxyFactoryBean.setServiceClass(WebService1HttpGet.class);
//        // 创建一个代理接口实现
//        webService1HttpGet = (WebService1HttpGet) jaxWsProxyFactoryBean.create();
//        return webService1HttpGet.writeDeliveryDetails(jsonVoiceArray,projectID.toString());

        return null;
    }

    @Override
    public String writeMakeInventoryDetails(String jsonVoiceArray, String projectID) throws Exception{

        return null;
    }

    @Override
    public String writeIssueDetails(String jsonVoiceArray, String projectID) throws Exception{
        return null;
    }

    @Override
    public String writeMoveInventoryDetails(String jsonVoiceArray, String projectID) throws Exception{
        return null;
    }

    @Override
    public String writePackingLists(String jsonVoiceArray, String projectID) throws Exception{

        return null;
    }
}
