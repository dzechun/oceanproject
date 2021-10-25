package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupReDc;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroupReDc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqEquipmentDataGroupReDcMapper;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentDataGroupReDcMapper;
import com.fantechs.provider.daq.service.DaqEquipmentDataGroupReDcService;
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
public class DaqEquipmentDataGroupReDcServiceImpl extends BaseService<DaqEquipmentDataGroupReDc> implements DaqEquipmentDataGroupReDcService {

    @Resource
    private DaqEquipmentDataGroupReDcMapper daqEquipmentDataGroupReDcMapper;
    @Resource
    private DaqHtEquipmentDataGroupReDcMapper daqHtEquipmentDataGroupReDcMapper;

    @Override
    public List<DaqEquipmentDataGroupReDcDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return daqEquipmentDataGroupReDcMapper.findList(map);
    }




    @Override
    public int batchAdd(List<DaqEquipmentDataGroupReDc> daqEquipmentDataGroupReDcs ) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<DaqEquipmentDataGroupReDc> ins = new ArrayList<DaqEquipmentDataGroupReDc>();
        List<DaqHtEquipmentDataGroupReDc> daqHtEquipmentDataGroupReDcs = new ArrayList<DaqHtEquipmentDataGroupReDc>();

        for(DaqEquipmentDataGroupReDc dc : daqEquipmentDataGroupReDcs) {
            Example example = new Example(DaqEquipmentDataGroupReDc.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("equipmentDataGroupId", dc.getEquipmentDataGroupId());
            daqEquipmentDataGroupReDcMapper.deleteByExample(example);
            example.clear();

            dc.setOrgId(user.getOrganizationId());
            dc.setCreateUserId(user.getUserId());
            dc.setCreateTime(new Date());
            dc.setModifiedUserId(user.getUserId());
            dc.setModifiedTime(new Date());
            dc.setStatus(StringUtils.isEmpty(dc.getStatus())?1: dc.getStatus());
            ins.add(dc);
            DaqHtEquipmentDataGroupReDc daqHtEquipmentDataGroupReDc =new DaqHtEquipmentDataGroupReDc();
            BeanUtils.copyProperties(dc, daqHtEquipmentDataGroupReDc);
            daqHtEquipmentDataGroupReDcs.add(daqHtEquipmentDataGroupReDc);
        }
        int i = 0;
        if(StringUtils.isNotEmpty(ins)) {
            i = daqEquipmentDataGroupReDcMapper.insertList(ins);
        }
        //新增设备分组历史信息
        if(StringUtils.isNotEmpty(daqHtEquipmentDataGroupReDcs))
            daqHtEquipmentDataGroupReDcMapper.insertList(daqHtEquipmentDataGroupReDcs);
        return i;
    }

    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
