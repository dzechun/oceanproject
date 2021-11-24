package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquPointInspectionOrderDetMapper;
import com.fantechs.provider.eam.mapper.EamHtEquPointInspectionOrderDetMapper;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderDetService;
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
public class EamEquPointInspectionOrderDetServiceImpl extends BaseService<EamEquPointInspectionOrderDet> implements EamEquPointInspectionOrderDetService {

    @Resource
    private EamEquPointInspectionOrderDetMapper eamEquPointInspectionOrderDetMapper;

    @Resource
    private EamHtEquPointInspectionOrderDetMapper eamHtEquPointInspectionOrderDetMapper;

    @Override
    public List<EamEquPointInspectionOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquPointInspectionOrderDetMapper.findList(map);
    }

    @Override
    public List<EamEquPointInspectionOrderDetDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquPointInspectionOrderDetMapper.findList(map);
    }

    @Override
    @Transactional
    public int batchSave(List<EamEquPointInspectionOrderDet> items){
        // 保存点检单明细
        eamEquPointInspectionOrderDetMapper.insertList(items);

        // 保存点检项目事项履历
        List<EamHtEquPointInspectionOrderDet> htOrderDets = items.stream().map(item -> {
            EamHtEquPointInspectionOrderDet eamHtEquPointInspectionOrderDet = new EamHtEquPointInspectionOrderDet();
            BeanUtil.copyProperties(item, eamHtEquPointInspectionOrderDet);
            return eamHtEquPointInspectionOrderDet;
        }).collect(Collectors.toList());
        return eamHtEquPointInspectionOrderDetMapper.insertList(htOrderDets);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        return user;
    }
}
