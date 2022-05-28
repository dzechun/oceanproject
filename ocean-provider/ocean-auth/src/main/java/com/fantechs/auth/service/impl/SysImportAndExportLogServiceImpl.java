package com.fantechs.auth.service.impl;

import com.fantechs.auth.service.SysImportAndExportLogService;
import com.fantechs.common.base.dto.security.SysImportAndExportLogDto;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.auth.mapper.SysImportAndExportLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

;

/**
 *
 * Created by leifengzhi on 2021/12/07.
 */
@Service
public class SysImportAndExportLogServiceImpl extends BaseService<SysImportAndExportLog> implements SysImportAndExportLogService {

    @Resource
    private SysImportAndExportLogMapper sysImportAndExportLogMapper;

    @Override
    public List<SysImportAndExportLogDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return sysImportAndExportLogMapper.findList(map);
    }

    @Override
    public int save(SysImportAndExportLog sysImportAndExportLog){

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        sysImportAndExportLog.setOrgId(user.getOrganizationId());
        sysImportAndExportLog.setCreateTime(new Date());
        sysImportAndExportLog.setCreateUserId(user.getUserId());
        sysImportAndExportLog.setModifiedTime(new Date());
        sysImportAndExportLog.setModifiedUserId(user.getUserId());

        return sysImportAndExportLogMapper.insertSelective(sysImportAndExportLog);
    }


}
