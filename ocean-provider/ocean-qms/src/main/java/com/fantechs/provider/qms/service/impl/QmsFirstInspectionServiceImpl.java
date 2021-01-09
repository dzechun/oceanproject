package com.fantechs.provider.qms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsFirstInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import com.fantechs.common.base.general.entity.qms.QmsDisqualification;
import com.fantechs.common.base.general.entity.qms.QmsFirstInspection;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.bills.StorageBillsFeignApi;
import com.fantechs.provider.qms.mapper.QmsDisqualificationMapper;
import com.fantechs.provider.qms.mapper.QmsFirstInspectionMapper;
import com.fantechs.provider.qms.service.QmsFirstInspectionService;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/06.
 */
@Service
public class QmsFirstInspectionServiceImpl extends BaseService<QmsFirstInspection> implements QmsFirstInspectionService {

    @Resource
    private QmsFirstInspectionMapper qmsFirstInspectionMapper;
    @Resource
    private QmsDisqualificationMapper qmsDisqualificationMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;


    @Override
    public List<QmsFirstInspectionDto> findList(Map<String, Object> map) {
        return qmsFirstInspectionMapper.findList (map);
    }



    @Override
    public int save(QmsFirstInspection qmsFirstInspection) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(QmsFirstInspection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("work_order_id", qmsFirstInspection.getWorkOrderId()).andEqualTo("inspectionResult",1);
        List<QmsFirstInspection> qmsFirstInspections = qmsFirstInspectionMapper.selectByExample(example);
        if (qmsFirstInspections.size()!=0){
            throw new BizErrorException("首检已通过，首检失败");
        }

        qmsFirstInspection.setCreateTime(new Date());
        qmsFirstInspection.setCreateUserId(user.getUserId());
        qmsFirstInspection.setModifiedTime(new Date());
        qmsFirstInspection.setDocumentsTime(new Date());
        qmsFirstInspection.setModifiedUserId(user.getUserId());
        qmsFirstInspection.setStatus(StringUtils.isEmpty(qmsFirstInspection.getStatus())?1:qmsFirstInspection.getStatus());
        qmsFirstInspection.setFirstInspectionCode(CodeUtils.getId("PDASJ"));

        int i = qmsFirstInspectionMapper.insert(qmsFirstInspection);

        List<QmsDisqualification> list = qmsFirstInspection.getList();

        if (list.size() !=0){
            Long maxLevel = 0L;
            for (QmsDisqualification qmsDisqualification : list) {
                qmsDisqualification.setCheckoutType((byte) 0);
                qmsDisqualification.setFirstInspectionIdId(qmsFirstInspection.getFirstInspectionId());
                qmsDisqualification.setCreateTime(new Date());
                qmsDisqualification.setCreateUserId(user.getUserId());
                qmsDisqualification.setModifiedTime(new Date());
                qmsDisqualification.setModifiedUserId(user.getUserId());
                qmsDisqualification.setStatus(StringUtils.isEmpty(qmsDisqualification.getStatus())?1:qmsDisqualification.getStatus());
                qmsDisqualification.setCheckoutType((byte) 0);
                if (qmsDisqualification.getLevel() > maxLevel){
                    maxLevel = qmsDisqualification.getLevel();
                }
            }
            qmsDisqualificationMapper.insertList(list);
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("severityLevel");
            ResponseEntity<List<SysSpecItem>> severityLevel = securityFeignApi.findSpecItemList(searchSysSpecItem);

            if (StringUtils.isNotEmpty(severityLevel) && StringUtils.isNotEmpty(severityLevel.getData()) && severityLevel.getData().size()!=0){
                SysSpecItem sysSpecItem = severityLevel.getData().get(0);
                String paraValue = sysSpecItem.getParaValue();
                String[] split = paraValue.split(",");
                for (String s : split) {
                    if (s.equals(maxLevel.toString())){
                        Long workOrderId = qmsFirstInspection.getWorkOrderId();
                        int i1 = 0;
                    }
                }

            }
        }
        return i;
    }

}
