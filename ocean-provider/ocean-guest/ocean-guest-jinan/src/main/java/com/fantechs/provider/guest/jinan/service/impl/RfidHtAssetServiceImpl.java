package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtAsset;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidHtAssetMapper;
import com.fantechs.provider.guest.jinan.service.RfidHtAssetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class RfidHtAssetServiceImpl extends BaseService<RfidHtAsset> implements RfidHtAssetService {

    @Resource
    private RfidHtAssetMapper rfidHtAssetMapper;

    @Override
    public List<RfidHtAsset> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidHtAssetMapper.findHtList(map);
    }

}
