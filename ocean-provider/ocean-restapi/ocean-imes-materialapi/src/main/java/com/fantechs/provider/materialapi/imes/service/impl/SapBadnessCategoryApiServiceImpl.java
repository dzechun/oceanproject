package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.DTMESBADCODE;
import com.fantechs.common.base.general.dto.restapi.DTMESBADCODEQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBADCODEQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapBadnessCategoryApi;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapBadnessCategoryApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi.SIMESBADCODEQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi.SIMESBADCODEQUERYOutService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@org.springframework.stereotype.Service
public class SapBadnessCategoryApiServiceImpl implements SapBadnessCategoryApiService {
    private static final Logger logger = LoggerFactory.getLogger(SapBadnessCategoryApiServiceImpl.class);

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private BaseUtils baseUtils;
    @Resource
    private LogsUtils logsUtils;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    @GlobalTransactional
    public int getbadnessCategory(SearchSapBadnessCategoryApi searchSapBadnessCategoryApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESBADCODEQUERYOutService service = new SIMESBADCODEQUERYOutService();
        SIMESBADCODEQUERYOut out = service.getHTTPPort();
        DTMESBADCODEQUERYREQ req = new DTMESBADCODEQUERYREQ();
        if(StringUtils.isEmpty(searchSapBadnessCategoryApi.getCatalogue()) || StringUtils.isEmpty(searchSapBadnessCategoryApi.getBadnessCodes()))
            throw new BizErrorException("目录和代码组不能为空");
        req.setKATALOGART(searchSapBadnessCategoryApi.getCatalogue());
        req.setCODEGRUPPE(searchSapBadnessCategoryApi.getBadnessCodes());
        DTMESBADCODEQUERYRES res = out.siMESBADCODEQUERYOut(req);
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE()) ){
            if(StringUtils.isEmpty(res.getBADCODE())) throw new BizErrorException("请求结果为空");
            //保存或更新不良类别
          /*  BaseBadnessCategory baseBadnessCategory = new BaseBadnessCategory();
            baseBadnessCategory.setBadnessCategoryCode(baseUtils.removeZero(searchSapBadnessCategoryApi.getBadnessCodes()));
            baseBadnessCategory.setBadnessCategoryDesc(searchSapBadnessCategoryApi.getCatalogue());
            baseBadnessCategory.setOrgId(orgId);
            baseBadnessCategory.setStatus((byte)1);
            baseBadnessCategory.setIsDelete((byte)1);
            ResponseEntity<BaseBadnessCategory> baseBadnessCategoryResponseEntity = baseFeignApi.saveByApi(baseBadnessCategory);*/
            List<BaseBadnessPhenotype> baseBadnessPhenotypes = new ArrayList<>();
            //保存不良现象代码
            for(DTMESBADCODE badCode: res.getBADCODE()) {
               /* BaseBadnessCause baseBadnessCause = new BaseBadnessCause();
                baseBadnessCause.setBadnessCauseCode(baseUtils.removeZero(badCode.getCODE()));
                baseBadnessCause.setBadnessCauseDesc(badCode.getKURZTEXT());
                baseBadnessCause.setBadnessCategoryId(baseBadnessCategoryResponseEntity.getData().getBadnessCategoryId());
                baseBadnessCause.setOrgId(orgId);
                baseFeignApi.saveByApi(baseBadnessCause);*/
                BaseBadnessPhenotype baseBadnessPhenotype = new BaseBadnessPhenotype();
                baseBadnessPhenotype.setBadnessPhenotypeCode(baseUtils.removeZero(badCode.getCODE()));
                baseBadnessPhenotype.setBadnessPhenotypeDesc(badCode.getKURZTEXT());
                baseBadnessPhenotype.setStatus((byte)1);
                baseBadnessPhenotype.setIsDelete((byte)1);
                //baseBadnessPhenotype.set(baseBadnessCategoryResponseEntity.getData().getBadnessCategoryId());
                baseBadnessPhenotype.setOrgId(orgId);
                baseBadnessPhenotypes.add(baseBadnessPhenotype);
            }
            baseFeignApi.saveByApi(baseBadnessPhenotypes);
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.getCODEGRUPPE()+"-"+req.getKATALOGART());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.getMESSAGE(),req.toString());
            throw new BizErrorException("接口请求失败");
        }
    }

}
