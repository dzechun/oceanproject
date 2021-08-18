package com.fantechs.provider.baseapi.esop.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.restapi.esop.EsopIssueRes;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.SkipHttpsUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.baseapi.esop.service.EsopIssueApiService;
import com.fantechs.provider.baseapi.esop.util.BaseUtils;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;


@org.springframework.stereotype.Service
public class EsopIssueApiServiceImpl implements EsopIssueApiService {
    private static final Logger log = LoggerFactory.getLogger(EsopLineServiceImpl.class);

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;
    @Resource
    private EamFeignApi eamFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;


    private String url = "http://xbqms.donlim.com:8081/qms/qualitiy/esop/defect"; //新宝工单接口地址

    @Override
    @LcnTransaction
    public int getIssue(String materialCode) {
        if (StringUtils.isEmpty(materialCode)) throw new BizErrorException("物料（产品）编码不能为空");
        String result = null;
        try {
            URL realUrl = new URL(url +"?productModel="+ materialCode);
            CloseableHttpClient httpClient = SkipHttpsUtil.wrapClient();
            HttpGet get = new HttpGet(String.valueOf(realUrl));
            CloseableHttpResponse response = null;

            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setOrgId(baseUtils.getOrId());
            ResponseEntity<List<BaseMaterial>> baseMaterials = baseFeignApi.findList(searchBaseMaterial);

            if(StringUtils.isEmpty(baseMaterials.getData())) throw new BizErrorException("同步错误，未查询到对应的产品型号，请先同步产品型号");
            try {
                response = httpClient.execute(get);
                if (response != null && response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                    Map<String, Object> map = JsonUtils.jsonToMap(result);
                    Map<String, Object> data = (Map<String, Object>) map.get("data");
                    List<Map<String,Object>> list = (List<Map<String,Object>>) data.get("data");
                    List<EamIssue> eamIssues = new ArrayList<>();
                    for(int i=0;i<list.size();i++){
                        EamIssue issue = new EamIssue();
                        issue.setIssueName((String)list.get(i).get("defect_name"));
                        issue.setRemark((String)list.get(i).get("num"));
                        issue.setMaterialId(baseMaterials.getData().get(0).getMaterialId());
                        issue.setCreateTime(new Date());
                        issue.setModifiedTime(new Date());
                        issue.setOrgId(baseUtils.getOrId());
                        eamIssues.add(issue);
                    }
                   eamFeignApi.batchAdd(eamIssues);
                }
                return 1;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    httpClient.close();
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    return 0;
    }

}
