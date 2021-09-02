package com.fantechs.provider.chinafiveringapi.api.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.chinafiveringapi.api.service.ImportDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class ImportDataServiceImpl implements ImportDataService {

    // Webservice接口地址
    private final String address = "http://mattest.cwcec.com/LocWebServices/WebService1.asmx";

    @Resource
    BaseFeignApi baseFeignApi;
    @Resource
    EngFeignApi engFeignApi;

    @Override
    public BaseExecuteResultDto getPoDetails(String projectID) throws Exception {
//        WebService1HttpGet webService1HttpGet=null;
//        // 代理工厂
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//        // 设置代理地址
//        jaxWsProxyFactoryBean.setAddress(address);
//        // 设置接口类型
//        jaxWsProxyFactoryBean.setServiceClass(WebService1HttpGet.class);
//        // 创建一个代理接口实现
//        webService1HttpGet = (WebService1HttpGet) jaxWsProxyFactoryBean.create();
//        return webService1HttpGet.getPoDetails(projectID.toString());

        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            baseExecuteResultDto= callWebService(address,"getPoDetails",projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            //转换为实体类集合
            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("材料用途","materialPurpose");
            String s1=s0.replaceAll("合同号","contractCode");
            String s2=s1.replaceAll("材料编码","materialCode");
            String s3=s2.replaceAll("位号","locationNum");
            String s4=s3.replaceAll("采购量","purQty");
            String s5=s4.replaceAll("备注","remark");
            String s6=s5.replaceAll("装置号","deviceCode");
            String s7=s6.replaceAll("主项号","dominantTermCode");
            String s8=s7.replaceAll("PPGUID","option1");
            String s9=s8.replaceAll("PSGUID","option2");
            String s10=s9.replaceAll("RDGUID","option3");

            //同步到数据库
            int indexb=s10.indexOf("[");
            int indexe=s10.lastIndexOf("]");
            String str=s10.substring(indexb,indexe+1);
            List<EngContractQtyOrder> listPO= BeanUtils.jsonToListObject(str,EngContractQtyOrder.class);
            for (EngContractQtyOrder engContractQtyOrder : listPO) {
                engContractQtyOrder.setOrgId(1004L);
                engFeignApi.saveByApi(engContractQtyOrder);
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    public String getIssueDetails(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        baseExecuteResultDto= callWebService(address,"getIssueDetails",projectID);
        if(baseExecuteResultDto.getIsSuccess()==false)
            throw new Exception(baseExecuteResultDto.getFailMsg());
        return baseExecuteResultDto.getExecuteResult().toString();
    }

    @Override
    public String getPartNoInfo(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        baseExecuteResultDto= callWebService(address,"getPartNoInfo",projectID);
        if(baseExecuteResultDto.getIsSuccess()==false)
            throw new Exception(baseExecuteResultDto.getFailMsg());
        return baseExecuteResultDto.getExecuteResult().toString();
    }

    @Override
    public String getShelvesNo(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        baseExecuteResultDto= callWebService(address,"getShelvesNo",projectID);
        if(baseExecuteResultDto.getIsSuccess()==false)
            throw new Exception(baseExecuteResultDto.getFailMsg());
        return baseExecuteResultDto.getExecuteResult().toString();
    }

    @Override
    public String getSubcontractor(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        baseExecuteResultDto= callWebService(address,"getSubcontractor",projectID);
        if(baseExecuteResultDto.getIsSuccess()==false)
            throw new Exception(baseExecuteResultDto.getFailMsg());
        return baseExecuteResultDto.getExecuteResult().toString();
    }

    @Override
    public BaseExecuteResultDto getVendor() throws Exception{
        BaseExecuteResultDto baseExecuteResultDto = new BaseExecuteResultDto();
        try {
            baseExecuteResultDto = callWebService(address, "getVendor", "");
            if (baseExecuteResultDto.getIsSuccess() == false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            String strResult = baseExecuteResultDto.getExecuteResult().toString();
            String s0 = strResult.replaceAll("企业中文名称", "supplierName");
            String s1 = s0.replaceAll("厂商企标ID", "supplierCode");

            int indexb = s1.indexOf("[");
            int indexe = s1.lastIndexOf("]");
            String str = s1.substring(indexb, indexe + 1);
            List<BaseSupplier> listSu = BeanUtils.jsonToListObject(str, BaseSupplier.class);

            //同步到数据库
            for (BaseSupplier baseSupplier : listSu) {
                baseSupplier.setOrganizationId(1004L);
                baseFeignApi.saveByApi(baseSupplier);
            }

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    public String getReqDetails(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        baseExecuteResultDto= callWebService(address,"getReqDetails",projectID);
        if(baseExecuteResultDto.getIsSuccess()==false)
            throw new Exception(baseExecuteResultDto.getFailMsg());
        return baseExecuteResultDto.getExecuteResult().toString();
    }

    private  String actionBySOAP(String method,String projectID){
        String str="";
        if(StringUtils.isEmpty(projectID)){
            str="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:"+method+"/>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
        }
        else {
            str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:" + method + ">\n" +
                    "         <tem:projectID>" + projectID + "</tem:projectID>\n" +
                    "      </tem:" + method + ">\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

        }

        return str;
    }

    private BaseExecuteResultDto callWebService(String wsdl, String method, String projectID) throws Exception {
        StringBuilder sb = new StringBuilder();
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "20000");
            System.setProperty("sun.net.client.defaultReadTimeout", "20000");

            // URL连接
            URL url = new URL(wsdl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // conn.setRequestMethod("GET");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(actionBySOAP(method, projectID).getBytes().length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=GBK");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            // 请求输入内容
            OutputStream output = conn.getOutputStream();
            output.write(actionBySOAP(method, projectID).getBytes());
            output.flush();
            output.close();
            // 请求返回内容
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str + "\n");
            }
            br.close();
            isr.close();

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setExecuteResult(sb.toString());

        }catch (Exception ex) {
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

}
