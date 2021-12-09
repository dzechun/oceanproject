package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmHtDeliveryAppointDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryAppoint;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.SrmHtDeliveryAppointMapper;
import com.fantechs.provider.srm.service.SrmHtDeliveryAppointService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */
@Service
public class SrmHtDeliveryAppointServiceImpl extends BaseService<SrmHtDeliveryAppoint> implements SrmHtDeliveryAppointService {

    @Resource
    private SrmHtDeliveryAppointMapper srmHtDeliveryAppointMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<SrmHtDeliveryAppointDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(sysUser.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if (StringUtils.isNotEmpty(list.getData())){
            map.put("supplierIdList", list.getData());
        }
        return srmHtDeliveryAppointMapper.findList(map);
    }
}
