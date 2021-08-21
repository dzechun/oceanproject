package com.fantechs.provider.baseapi.esop.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.SkipHttpsUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.baseapi.esop.service.EsopIssueApiService;
import com.fantechs.provider.baseapi.esop.util.BaseUtils;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
        Long orgId = baseUtils.getOrId();
        try {
            URL realUrl = new URL(url +"?productModel="+ materialCode);
            CloseableHttpClient httpClient = SkipHttpsUtil.wrapClient();
            HttpGet get = new HttpGet(String.valueOf(realUrl));
            CloseableHttpResponse response = null;
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setOrgId(orgId);
            ResponseEntity<List<BaseMaterial>> baseMaterials = baseFeignApi.findList(searchBaseMaterial);

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
                        issue.setStatus((byte)1);
                        eamIssues.add(issue);
                    }
                   eamFeignApi.batchAdd(eamIssues);
                }
              //  logsUtils.addlog((byte)1,(byte)1,orgId,result,materialCode);
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

    @Override
    public int getAllIssue(SearchBaseMaterial searchBaseMaterial) {
        searchBaseMaterial.setOrgId(baseUtils.getOrId());
        ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
        if(StringUtils.isNotEmpty(list.getData())){
            for(BaseMaterial baseMaterial : list.getData()){
                this.getIssue(baseMaterial.getMaterialCode());
            }
        }
        return 1;
    }

}
