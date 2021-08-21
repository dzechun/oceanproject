package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProjectItem;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionProjectItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquPointInspectionProjectItemMapper;
import com.fantechs.provider.eam.mapper.EamHtEquPointInspectionProjectItemMapper;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquPointInspectionProjectItemServiceImpl extends BaseService<EamEquPointInspectionProjectItem> implements EamEquPointInspectionProjectItemService {

    @Resource
    private EamEquPointInspectionProjectItemMapper eamEquPointInspectionProjectItemMapper;

    @Resource
    private EamHtEquPointInspectionProjectItemMapper eamHtEquPointInspectionProjectItemMapper;

    @Override
    public List<EamEquPointInspectionProjectItemDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquPointInspectionProjectItemMapper.findList(map);
    }

    @Override
    public List<EamEquPointInspectionProjectItemDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquPointInspectionProjectItemMapper.findList(map);
    }

    @Override
    @Transactional
    public int batchSave(List<EamEquPointInspectionProjectItem> items){
        // 保存点检项目事项
        eamEquPointInspectionProjectItemMapper.insertList(items);

        // 保存点检项目事项履历
        List<EamHtEquPointInspectionProjectItem> htProjectItemList = items.stream().map(item -> {
            EamHtEquPointInspectionProjectItem eamHtEquPointInspectionProjectItem = new EamHtEquPointInspectionProjectItem();
            BeanUtil.copyProperties(item, eamHtEquPointInspectionProjectItem);
            return eamHtEquPointInspectionProjectItem;
        }).collect(Collectors.toList());
        return eamHtEquPointInspectionProjectItemMapper.insertList(htProjectItemList);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
