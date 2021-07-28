package com.fantechs.provider.smt.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasteJobDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPasteJob;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.smt.mapper.SmtSolderPasteJobMapper;
import com.fantechs.provider.smt.service.SmtSolderPasteJobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/27.
 */
@Service
public class SmtSolderPasteJobServiceImpl extends BaseService<SmtSolderPasteJob> implements SmtSolderPasteJobService {

    @Resource
    private SmtSolderPasteJobMapper smtSolderPasteJobMapper;

    @Override
    public List<SmtSolderPasteJobDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",sysUser.getOrganizationId());
        return smtSolderPasteJobMapper.findList(map);
    }
}
