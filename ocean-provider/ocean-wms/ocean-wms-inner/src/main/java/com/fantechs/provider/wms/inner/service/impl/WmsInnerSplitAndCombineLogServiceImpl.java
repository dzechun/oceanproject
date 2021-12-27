package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerSplitAndCombineLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerSplitAndCombineLogMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerSplitAndCombineLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/27.
 */
@Service
public class WmsInnerSplitAndCombineLogServiceImpl extends BaseService<WmsInnerSplitAndCombineLog> implements WmsInnerSplitAndCombineLogService {

    @Resource
    private WmsInnerSplitAndCombineLogMapper wmsInnerSplitAndCombineLogMapper;

    @Override
    public List<WmsInnerSplitAndCombineLog> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return wmsInnerSplitAndCombineLogMapper.findList(map);
    }
}
