package com.fantechs.provider.baseapi.esop.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.mapper.EsopDeptMapper;
import com.fantechs.provider.baseapi.esop.service.EsopDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@Service
public class EsopDeptServiceImpl extends BaseService<EsopDept> implements EsopDeptService {

    @Resource
    private EsopDeptMapper esopDeptMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @LcnTransaction
    public int addDept(Map<String, Object> map) {
        List<EsopDept> list = esopDeptMapper.findList(map);
        List<BaseDept> baseDepts = new ArrayList<BaseDept>();
        if(StringUtils.isNotEmpty(list)){
            for(EsopDept dept :list){
                baseDepts.add(getBaseDepy(dept));
            }
        }
        baseFeignApi.batchAddDept(baseDepts);
        return 1;
    }


    public BaseDept getBaseDepy(EsopDept esopDept){
        BaseDept baseDept = new BaseDept();
        baseDept.setDeptName(esopDept.getName());
        baseDept.setDeptCode(esopDept.getCode());
        baseDept.setDeptDesc(esopDept.getName());
        baseDept.setCreateTime(esopDept.getCreatedTime());
        baseDept.setModifiedTime(esopDept.getModifyTime());
        baseDept.setIsDelete(esopDept.getIsDeleted());
        baseDept.setOrganizationId((long)1002);
        return baseDept;
    }

}
