package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManageBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.qms.mapper.QmsBadnessManageBarcodeMapper;
import com.fantechs.provider.qms.service.QmsBadnessManageBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class QmsBadnessManageBarcodeServiceImpl extends BaseService<QmsBadnessManageBarcode> implements QmsBadnessManageBarcodeService {

    @Resource
    private QmsBadnessManageBarcodeMapper qmsBadnessManageBarcodeMapper;

    @Override
    public List<QmsBadnessManageBarcode> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsBadnessManageBarcodeMapper.findList(map);
    }

}
