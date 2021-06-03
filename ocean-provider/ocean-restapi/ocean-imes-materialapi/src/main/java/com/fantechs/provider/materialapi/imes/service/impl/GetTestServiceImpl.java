package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.provider.materialapi.imes.service.GetTestService;

import java.io.IOException;
/*import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.rmi.RemoteException;*/

//import org.apache.axis.client.Service;



@org.springframework.stereotype.Service
public class GetTestServiceImpl implements GetTestService {


    @Override
    public String getApache() {
       /* Service service = new Service();
        userName = "MESPIALEUSER";
        password = "1234qwer";
        try {
            Call call = (Call) service.createCall();
            //设置地址
            // call.setTargetEndpointAddress("http://127.0.0.1:8768/ocean-imes-materialapi/material/api?wsdl");
            call.setTargetEndpointAddress("http://sappodev.leisai.com:50000/dir/wsdl?p=ic/098215a2ef473d4aaa6e4701f24f3cde");
            call.setUseSOAPAction(true);
            call.setUsername(Base64.encode(userName.getBytes()).toString());
            call.setPassword(Base64.encode(password.getBytes()).toString());
            String userPass = "MESPIALEUSER:1234qwer";
            //call.addHeader(getHoapHeader());
            Authenticator.setDefault(new BasicAuthenticator(userName, password));
            String str = "TUVTUElBTEVVU0VSOjEyMzRxd2Vy";
            call.setProperty("Authorization", "Basic " + Base64.encode(userPass.getBytes()));

            System.out.println("---base-----"+Base64.encode(userPass.getBytes()).length);
            System.out.println("---base1-----"+Base64.encode(userPass.getBytes()).toString());
            //call.addHeader(new SOAPHeaderElement(new QName("http://leisai.com/None_ECC","SecurityHeader"),"Basic " + Base64.encode(userPass.getBytes())));
            //域名加方法,//上面有写着targetNamespace="http://x.x.x/",这个就是你的命名空间值了;加方法名
            call.setSOAPActionURI("http://leisai.com/None_ECC" + "MT_MES_MATERIAL_QUERY_RES");

            // 设置要调用哪个方法
            call.setOperationName(new QName("http://leisai.com/None_ECC", "MT_MES_MATERIAL_QUERY_RES")); // 设置要调用哪个方法
            //设置参数名 :参数名 ,参数类型:String, 参数模式：'IN' or 'OUT'
            call.addParameter(new QName("http://leisai.com/None_ECC","ERSDA"),// 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName("http://leisai.com/None_ECC","ERSDAEND"),// 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

            call.setEncodingStyle("UTF-8");
            //返回类型
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// （标准的类型）

            // 调用方法并传递参数
          //  String res = String.valueOf(call.invoke(new Object[]{20210110,20210120}));
            String[] data = {"20210110","20210111"};

            String res = String.valueOf(call.invoke(new Object[]{"20210110","20210111"}));
            System.out.println(res);

        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        return null;
    }


    @Override
    public String getHttp() throws IOException {
   /*     //第一步：创建服务地址，不是WSDL地址
        //URL url = new URL("http://192.168.204.155:8768/ocean-imes-materialapi/material/api?wsdl");
        URL url = new URL("http://sappodev.leisai.com:50000/dir/wsdl?p=ic/098215a2ef473d4aaa6e4701f24f3cde");
        //2：打开到服务地址的一个连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //3：设置连接参数
        //3.1设置发送方式：POST必须大写
        connection.setRequestMethod("POST");
        //3.2设置数据格式：Content-type
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        //3.3设置输入输出，新创建的connection默认是没有读写权限的，
        connection.setDoInput(true);
        connection.setDoOutput(true);
        userName = "MESPIALEUSER";
        password = "1234qwer";
        connection.setRequestProperty("Username",userName);
        connection.setRequestProperty("Password",password);
        connection.setRequestProperty("Authenticator",password);
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        String userPass = "MESPIALEUSER:1234qwer";
        connection.setRequestProperty("Authorization", "Basic " + Base64.encode(userPass.getBytes()));
        //4：组织SOAP协议数据，发送给服务端
        String soapXML = getXML("20210110","20210111");
        OutputStream os = connection.getOutputStream();
        os.write(soapXML.getBytes());

        //5：接收服务端的响应
        int responseCode = connection.getResponseCode();
        System.out.println("------------------responseCode-----------------------"+responseCode);

        if(200 == responseCode){//表示服务端响应成功
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String temp = null;

            while(null != (temp = br.readLine())){
                sb.append(temp);
            }

            System.out.println("-----------------------------------------"+sb.toString());

            is.close();
            isr.close();
            br.close();
        }

        os.close();*/
        return null;
    }

    @Override
    public String getApacheMes() {
      /*  Service service = new Service();
        try {
            Call call = (Call) service.createCall();
            //设置地址
            call.setTargetEndpointAddress("http://192.168.204.155:8768/ocean-imes-materialapi/material/api?wsdl");
            call.setUseSOAPAction(true);
            *//*call.setUsername("MESPIALEUSER");
            call.setPassword("1234qwer");
            String userPass = "MESPIALEUSER:1234qwer";
            call.addHeader(new SOAPHeaderElement(new QName("http://leisai.com/None_ECC","SecurityHeader"),"Basic " + Base64.encode(userPass.getBytes())));*//*
            //call.addHeader(getHoapHeader());
            //域名加方法,//上面有写着targetNamespace="http://x.x.x/",这个就是你的命名空间值了;加方法名
            call.setSOAPActionURI("http://imes.materialapi.provider.fantechs.com/" + "MaterialService");

            // 设置要调用哪个方法
            call.setOperationName(new QName("http://imes.materialapi.provider.fantechs.com", "testMethod")); // 设置要调用哪个方法
            //call.setOperationName(new QName("http://leisai.com/None_ECC", "MT_MES_MATERIAL_QUERY_RES")); // 设置要调用哪个方法
            //设置参数名 :参数名 ,参数类型:String, 参数模式：'IN' or 'OUT'
            call.addParameter(new QName("http://imes.materialapi.provider.fantechs.com", "testName"),// 设置要传递的参数
                    org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);


            call.setEncodingStyle("UTF-8");
            //返回类型
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// （标准的类型）

            // 调用方法并传递参数

            String res = String.valueOf(call.invoke(new Object[]{"20210110"}));
            System.out.println("-------------------------------------"+res);

        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        return null;
    }


    public static String getXML(String startTime,String endTime){
       /* String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                +"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:imes=\"http://imes.materialapi.provider.fantechs.com\">"
                +"<soapenv:Header/>"
                +"<soapenv:Body>"
                +"<imes:testMethod>"
                +"<testName>"+startTime+"</testName>"
                +"</imes:testMethod>"
                +"</soapenv:Body>"
                +"</soapenv:Envelope>";*/

        String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                +"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope\" xmlns:ecc=\"http://leisai.com/ECC\">"
                +"<soapenv:Header/>"
                +"<soapenv:Body>"
                +"<ecc:MT_MES_MATERIAL_QUERY_REQ>"
                +"<ERSDA>"+startTime+"</ERSDA>"
                +"<ERSDAEND>"+endTime+"</ERSDAEND>"
                +"</ecc:MT_MES_MATERIAL_QUERY_REQ>"
                +"</soapenv:Body>"
                +"</soapenv:Envelope>";
        return soapXML;
    }

/*
    public SOAPHeaderElement getHoapHeader() throws SOAPException {

        SOAPHeaderElement element = new SOAPHeaderElement(new QName(
                "http://leisai.com/None_ECC", "ValidHeader"));
        SOAPElement se = element.addChildElement("Username");
        se.addTextNode("MESPIALEUSER");
        se = element.addChildElement("Password");
        se.addTextNode("1234qwer");
        return element;
    }
*/


    @Override
    public String getLeisai() {
        String  userName = "MESPIALEUSER";
        String password = "1234qwer";

       /* SIMESMATERIALQUERYOutService service = new SIMESMATERIALQUERYOutService();
        SIMESMATERIALQUERYOut out = service.getHTTPPort();
       // SIMESMATERIALQUERYOut methodService = service.getPort(SIMESMATERIALQUERYOut.class);
        System.out.println("---out---"+out);
        DTMESMATERIALQUERYREQ req = new DTMESMATERIALQUERYREQ();
        req.setERSDA("20210110");
        req.setERSDAEND("20210111");
        DTMESMATERIALQUERYRES res = out.siMESMATERIALQUERYOut(req);
        System.out.println("---res---"+res);*/

       /* DTMESMATERIALQUERYREQ req = new DTMESMATERIALQUERYREQ();
        req.setERSDA("20210110");
        req.setERSDAEND("20210111");
        DTMESMATERIALQUERYRES res = out.siMESMATERIALQUERYOut(req);*/


        /*String nameSpaceURI = "http://leisai.com/None_ECC";
        String wsUrl = "http://sappodev.leisai.com:50000/dir/wsdl?p=ic/7777ff25448a38b48df4a9389df8f606";
        String localPart = "SI_CREMOD_CUSTOMER_OutService";
        Service service = ServiceUtil.getService(wsUrl,nameSpaceURI,localPart);
        SIMESMATERIALQUERYOut methodService = service.getPort(SIMESMATERIALQUERYOut.class);

        DTMESMATERIALQUERYREQ customInfo = new DTMESMATERIALQUERYREQ();
        customInfo.setERSDA("20210110");
        customInfo.setERSDAEND("20210111");*/

        /*SIMESMATERIALQUERYOutService customerService = new SIMESMATERIALQUERYOutService();
        DTMESMATERIALQUERYRES resultMsg = customerService.siMESMATERIALQUERYOut(customInfo);*/
      //  System.out.println("雷赛客户数据返回值："+ resultMsg);
        return null;
    }


    @Override
    public String getAxis2() {
        /*String endpoint = "http://sappodev.leisai.com:50000/dir/wsdl?p=ic/7777ff25448a38b48df4a9389df8f606";
        String res = null;
        // 查询电话号码的接口方法名
        String operationName = "MT_MES_MATERIAL_QUERY_RES";
        // 定义service对象
        Service service = new Service();
        // 创建一个call对象
        Call call = (Call) service.createCall();
        // 设置目标地址，即webservice路径
        call.setTargetEndpointAddress(endpoint);
        // 设置操作名称，即方法名称   targetNamespace="http://WebXml.com.cn/"
        call.setOperationName(new QName("http://leisai.com/None_ECC", operationName));
        // 设置方法参数
        call.addParameter(new QName("http://leisai.com/None_ECC", "ERSDA"),
                org.apache.axis.encoding.XMLType.XSD_STRING,
                javax.xml.rpc.ParameterMode.IN);
        call.addParameter(new QName("http://leisai.com/None_ECC", "ERSDAEND"),
                org.apache.axis.encoding.XMLType.XSD_STRING,
                javax.xml.rpc.ParameterMode.IN);
        // 设置返回值类型
        //对于返回是字符串数组的返回类型只有这两种可行

        //call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_VECTOR);
        call.setReturnClass(java.lang.String.class);

        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://leisai.com/None_ECC" + "MT_MES_MATERIAL_QUERY_RES");

        res = (String) call.invoke(new Object[]{"20210110", "20210111"});

        // 如果返回类型是org.apache.axis.encoding.XMLType.SOAP_VECTOR时用下面的转型接收
        //Vector v=(Vector) call.invoke(new Object[]{cityCode,userId});

        System.out.println(res);*/


        return null;
    }

}
