package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtClientManageDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.electronic.service.SmtClientManageService;
import com.fantechs.provider.electronic.mapper.SmtClientManageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/01.
 */
@Service
public class SmtClientManageServiceImpl extends BaseService<SmtClientManage> implements SmtClientManageService {

    @Resource
    private SmtClientManageMapper smtClientManageMapper;

    @Override
    public int save(SmtClientManage smtClientManage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        smtClientManage.setCreateUserId(user.getUserId());
        smtClientManage.setCreateTime(new Date());
        smtClientManage.setModifiedUserId(user.getUserId());
        smtClientManage.setModifiedTime(new Date());
        smtClientManage.setSecretKey(UUIDUtils.getUUID());
        smtClientManageMapper.insertUseGeneratedKeys(smtClientManage);
        smtClientManage.setQueueName("ocean.ablepick" + smtClientManage.getClientId());
        return smtClientManageMapper.updateByPrimaryKeySelective(smtClientManage);
    }

    @Override
    public int update(SmtClientManage smtClientManage) {
        if (smtClientManage.getLoginTag()==0){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
        }
        return smtClientManageMapper.updateByPrimaryKeySelective(smtClientManage);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            SmtClientManage smtClientManage = smtClientManageMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtClientManage)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }
        return smtClientManageMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtClientManageDto> findList(Map<String, Object> map) {
        return smtClientManageMapper.findList(map);
    }
}
