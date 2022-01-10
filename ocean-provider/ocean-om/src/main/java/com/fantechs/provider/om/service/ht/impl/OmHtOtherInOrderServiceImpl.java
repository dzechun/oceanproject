package com.fantechs.provider.om.service.ht.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmHtOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderMapper;
import com.fantechs.provider.om.service.ht.OmHtOtherInOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/01/10.
 */
@Service
public class OmHtOtherInOrderServiceImpl extends BaseService<OmHtOtherInOrder> implements OmHtOtherInOrderService {

    @Resource
    private OmHtOtherInOrderMapper omHtOtherInOrderMapper;

    @Override
    public List<OmHtOtherInOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return omHtOtherInOrderMapper.findList(map);
    }
}
