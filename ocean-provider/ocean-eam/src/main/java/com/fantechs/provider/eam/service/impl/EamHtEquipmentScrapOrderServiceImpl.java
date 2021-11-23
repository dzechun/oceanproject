package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentScrapOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtEquipmentScrapOrderMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentScrapOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamHtEquipmentScrapOrderServiceImpl extends BaseService<EamHtEquipmentScrapOrder> implements EamHtEquipmentScrapOrderService {

    @Resource
    private EamHtEquipmentScrapOrderMapper eamHtEquipmentScrapOrderMapper;

    @Override
    public List<EamHtEquipmentScrapOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamHtEquipmentScrapOrderMapper.findList(map);
    }
}
