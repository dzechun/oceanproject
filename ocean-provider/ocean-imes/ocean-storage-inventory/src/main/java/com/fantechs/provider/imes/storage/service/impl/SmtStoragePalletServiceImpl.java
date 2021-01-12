package com.fantechs.provider.imes.storage.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.storage.mapper.SmtStoragePalletMapper;
import com.fantechs.provider.imes.storage.service.SmtStoragePalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class SmtStoragePalletServiceImpl extends BaseService<SmtStoragePallet> implements SmtStoragePalletService {

    @Resource
    private SmtStoragePalletMapper smtStoragePalletMapper;


    @Override
    public int save(SmtStoragePallet smtStoragePallet) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        smtStoragePallet.setOrganizationId(user.getOrganizationId());
        smtStoragePallet.setCreateTime(new Date());
        smtStoragePallet.setCreateUserId(user.getCreateUserId());

        return smtStoragePalletMapper.insertSelective(smtStoragePallet);
    }

    @Override
    public int update(SmtStoragePallet smtStoragePallet) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        smtStoragePallet.setModifiedTime(new Date());
        smtStoragePallet.setModifiedUserId(user.getUserId());
        return smtStoragePalletMapper.updateByPrimaryKeySelective(smtStoragePallet);
    }

    @Override
    public List<SmtStoragePalletDto> findList(Map<String, Object> map) {
        return smtStoragePalletMapper.findList(map);
    }
}
