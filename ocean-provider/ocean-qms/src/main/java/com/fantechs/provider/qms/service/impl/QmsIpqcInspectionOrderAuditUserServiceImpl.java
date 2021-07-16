package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderAuditUserMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderAuditUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */
@Service
public class QmsIpqcInspectionOrderAuditUserServiceImpl extends BaseService<QmsIpqcInspectionOrderAuditUser> implements QmsIpqcInspectionOrderAuditUserService {

    @Resource
    private QmsIpqcInspectionOrderAuditUserMapper qmsIpqcInspectionOrderAuditUserMapper;

    @Override
    public List<QmsIpqcInspectionOrderAuditUser> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());
        return qmsIpqcInspectionOrderAuditUserMapper.findList(map);
    }


    @Override
    public int save(QmsIpqcInspectionOrderAuditUser record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1:record.getStatus());
        record.setOrgId(user.getOrganizationId());
        return qmsIpqcInspectionOrderAuditUserMapper.insertUseGeneratedKeys(record);
    }
}
