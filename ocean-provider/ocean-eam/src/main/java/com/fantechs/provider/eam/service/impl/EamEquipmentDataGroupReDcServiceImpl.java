package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShop;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupReDc;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroupReDc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentDataGroupReDcMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentDataGroupReDcMapper;
import com.fantechs.provider.eam.service.EamEquipmentDataGroupReDcService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class EamEquipmentDataGroupReDcServiceImpl extends BaseService<EamEquipmentDataGroupReDc> implements EamEquipmentDataGroupReDcService {

    @Resource
    private EamEquipmentDataGroupReDcMapper eamEquipmentDataGroupReDcMapper;
    @Resource
    private EamHtEquipmentDataGroupReDcMapper eamHtEquipmentDataGroupReDcMapper;

    @Override
    public List<EamEquipmentDataGroupReDcDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentDataGroupReDcMapper.findList(map);
    }


    @Override
    public int batchAdd(List<EamEquipmentDataGroupReDc> eamEquipmentDataGroupReDcs ) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<EamEquipmentDataGroupReDc> ins = new ArrayList<EamEquipmentDataGroupReDc>();
        List<EamHtEquipmentDataGroupReDc> eamHtEquipmentDataGroupReDcs = new ArrayList<EamHtEquipmentDataGroupReDc>();

        for(EamEquipmentDataGroupReDc dc : eamEquipmentDataGroupReDcs) {
            Example example = new Example(EamEquipmentDataGroupReDc.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", user.getOrganizationId());
            criteria.andEqualTo("equipmentDataGroupId", dc.getEquipmentDataGroupId());
            eamEquipmentDataGroupReDcMapper.deleteByExample(example);
            example.clear();

            dc.setOrgId(user.getOrganizationId());
            dc.setCreateUserId(user.getUserId());
            dc.setCreateTime(new Date());
            dc.setModifiedUserId(user.getUserId());
            dc.setModifiedTime(new Date());
            dc.setStatus(StringUtils.isEmpty(dc.getStatus())?1: dc.getStatus());
            ins.add(dc);
            EamHtEquipmentDataGroupReDc eamHtEquipmentDataGroupReDc =new EamHtEquipmentDataGroupReDc();
            BeanUtils.copyProperties(dc, eamHtEquipmentDataGroupReDc);
            eamHtEquipmentDataGroupReDcs.add(eamHtEquipmentDataGroupReDc);
        }
        int i = 0;
        if(StringUtils.isNotEmpty(ins)) {
            i = eamEquipmentDataGroupReDcMapper.insertList(ins);
        }
        //新增车间历史信息
        if(StringUtils.isNotEmpty(eamHtEquipmentDataGroupReDcs))
            eamHtEquipmentDataGroupReDcMapper.insertList(eamHtEquipmentDataGroupReDcs);
        return i;
    }
}
