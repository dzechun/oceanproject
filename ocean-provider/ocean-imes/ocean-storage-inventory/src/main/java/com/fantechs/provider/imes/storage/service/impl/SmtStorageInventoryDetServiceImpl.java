package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryDetMapper;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryMapper;
import com.fantechs.provider.imes.storage.service.SmtStorageInventoryDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */
@Service
public class SmtStorageInventoryDetServiceImpl extends BaseService<SmtStorageInventoryDet> implements SmtStorageInventoryDetService {

    @Resource
    private SmtStorageInventoryDetMapper smtStorageInventoryDetMapper;

    @Override
    public List<SmtStorageInventoryDetDto> findList(Map<String, Object> map) {
        return smtStorageInventoryDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtStorageInventoryDet smtStorageInventoryDet) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUser)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        smtStorageInventoryDet.setCreateTime(new Date());
        smtStorageInventoryDet.setCreateUserId(currentUser == null?0:currentUser.getUserId());
        smtStorageInventoryDet.setModifiedTime(new Date());
        smtStorageInventoryDet.setModifiedUserId(currentUser == null?0:currentUser.getUserId());
        smtStorageInventoryDet.setStatus(StringUtils.isEmpty(smtStorageInventoryDet.getStatus())?1:smtStorageInventoryDet.getStatus());

        return smtStorageInventoryDetMapper.insertUseGeneratedKeys(smtStorageInventoryDet);
    }

    @Override
    public int update(SmtStorageInventoryDet smtStorageInventoryDet) {
        return smtStorageInventoryDetMapper.updateByPrimaryKeySelective(smtStorageInventoryDet);
    }
}
