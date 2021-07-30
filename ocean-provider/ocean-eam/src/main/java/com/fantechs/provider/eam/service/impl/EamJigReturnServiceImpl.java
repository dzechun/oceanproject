package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReturnDto;
import com.fantechs.common.base.general.entity.eam.EamJigReturn;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamJigReturnMapper;
import com.fantechs.provider.eam.service.EamJigReturnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */
@Service
public class EamJigReturnServiceImpl extends BaseService<EamJigReturn> implements EamJigReturnService {

    @Resource
    private EamJigReturnMapper eamJigReturnMapper;

    @Override
    public List<EamJigReturnDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigReturnMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigReturn record) {
        return super.save(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigReturn entity) {
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        return super.batchDelete(ids);
    }
}
