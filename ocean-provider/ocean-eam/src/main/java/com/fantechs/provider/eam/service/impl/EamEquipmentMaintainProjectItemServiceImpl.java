package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainProjectItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMaintainProjectItemMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaintainProjectItemMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectItemService;
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
public class EamEquipmentMaintainProjectItemServiceImpl extends BaseService<EamEquipmentMaintainProjectItem> implements EamEquipmentMaintainProjectItemService {

    @Resource
    private EamEquipmentMaintainProjectItemMapper eamEquipmentMaintainProjectItemMapper;

    @Resource
    private EamHtEquipmentMaintainProjectItemMapper eamHtEquipmentMaintainProjectItemMapper;

    @Override
    public List<EamEquipmentMaintainProjectItemDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMaintainProjectItemMapper.findList(map);
    }

    @Override
    public List<EamEquipmentMaintainProjectItemDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMaintainProjectItemMapper.findList(map);
    }

    @Override
    @Transactional
    public int batchSave(List<EamEquipmentMaintainProjectItem> items){
        // 保存保养项目事项
        eamEquipmentMaintainProjectItemMapper.insertList(items);

        // 保存保养项目事项履历
        List<EamHtEquipmentMaintainProjectItem> htProjectItemList = items.stream().map(item -> {
            EamHtEquipmentMaintainProjectItem eamHtEquipmentMaintainProjectItem = new EamHtEquipmentMaintainProjectItem();
            BeanUtil.copyProperties(item, eamHtEquipmentMaintainProjectItem);
            return eamHtEquipmentMaintainProjectItem;
        }).collect(Collectors.toList());
        return eamHtEquipmentMaintainProjectItemMapper.insertList(htProjectItemList);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        return user;
    }
}
