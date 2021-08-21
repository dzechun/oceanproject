package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMaintainOrderDetMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaintainOrderDetMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentMaintainOrderDetServiceImpl extends BaseService<EamEquipmentMaintainOrderDet> implements EamEquipmentMaintainOrderDetService {

    @Resource
    private EamEquipmentMaintainOrderDetMapper eamEquipmentMaintainOrderDetMapper;

    @Resource
    private EamHtEquipmentMaintainOrderDetMapper eamHtEquipmentMaintainOrderDetMapper;

    @Override
    public List<EamEquipmentMaintainOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMaintainOrderDetMapper.findList(map);
    }

    @Override
    public List<EamEquipmentMaintainOrderDetDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMaintainOrderDetMapper.findList(map);
    }

    @Override
    @Transactional
    public int batchSave(List<EamEquipmentMaintainOrderDet> items){
        // 保存保养单明细
        eamEquipmentMaintainOrderDetMapper.insertList(items);

        // 保存保养项目事项履历
        List<EamHtEquipmentMaintainOrderDet> htOrderDets = items.stream().map(item -> {
            EamHtEquipmentMaintainOrderDet eamHtEquipmentMaintainOrderDet = new EamHtEquipmentMaintainOrderDet();
            BeanUtil.copyProperties(item, eamHtEquipmentMaintainOrderDet);
            return eamHtEquipmentMaintainOrderDet;
        }).collect(Collectors.toList());
        return eamHtEquipmentMaintainOrderDetMapper.insertList(htOrderDets);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
