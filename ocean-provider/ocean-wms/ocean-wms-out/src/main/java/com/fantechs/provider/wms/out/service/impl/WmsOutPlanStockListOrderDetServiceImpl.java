package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;

import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanStockListOrderDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderDetService;
import io.micrometer.core.instrument.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@Service
public class WmsOutPlanStockListOrderDetServiceImpl extends BaseService<WmsOutPlanStockListOrderDet> implements WmsOutPlanStockListOrderDetService {

    @Resource
    private WmsOutPlanStockListOrderDetMapper wmsOutPlanStockListOrderDetMapper;

    @Override
    public List<WmsOutPlanStockListOrderDetDto> findList(SearchWmsOutPlanStockListOrderDet searchWmsOutPlanStockListOrderDet) {
        if(StringUtils.isEmpty(searchWmsOutPlanStockListOrderDet.getOrgId())){
            SysUser user=currentUser();
            searchWmsOutPlanStockListOrderDet.setOrgId(user.getOrganizationId());
        }
        return wmsOutPlanStockListOrderDetMapper.findList(searchWmsOutPlanStockListOrderDet);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
