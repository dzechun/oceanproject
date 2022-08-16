package com.fantechs.provider.om.service.ht.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmHtSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesCodeReSpc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.om.mapper.ht.OmHtSalesCodeReSpcMapper;
import com.fantechs.provider.om.service.ht.OmHtSalesCodeReSpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/15.
 */
@Service
public class OmHtSalesCodeReSpcServiceImpl extends BaseService<OmHtSalesCodeReSpc> implements OmHtSalesCodeReSpcService {

    @Resource
    private OmHtSalesCodeReSpcMapper omHtSalesCodeReSpcMapper;

    @Override
    public List<OmHtSalesCodeReSpcDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return omHtSalesCodeReSpcMapper.findList(map);
    }
}
