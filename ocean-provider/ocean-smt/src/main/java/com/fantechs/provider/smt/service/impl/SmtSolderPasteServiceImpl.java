package com.fantechs.provider.smt.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.smt.mapper.SmtSolderPasteMapper;
import com.fantechs.provider.smt.service.SmtSolderPasteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/07/22.
 */
@Service
public class SmtSolderPasteServiceImpl extends BaseService<SmtSolderPaste> implements SmtSolderPasteService {

    @Resource
    private SmtSolderPasteMapper smtSolderPasteMapper;

    @Override
    public List<SmtSolderPasteDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return smtSolderPasteMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SmtSolderPaste record) {
        SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        return super.save(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            SmtSolderPaste smtSolderPaste = smtSolderPasteMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtSolderPaste)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
        }
        return super.batchDelete(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
