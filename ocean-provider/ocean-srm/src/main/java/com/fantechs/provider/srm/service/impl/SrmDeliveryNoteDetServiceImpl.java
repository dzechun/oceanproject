package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDetDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmDeliveryNoteDetMapper;
import com.fantechs.provider.srm.service.SrmDeliveryNoteDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class SrmDeliveryNoteDetServiceImpl extends BaseService<SrmDeliveryNoteDet> implements SrmDeliveryNoteDetService {

    @Resource
    private SrmDeliveryNoteDetMapper srmDeliveryNoteDetMapper;

    @Override
    public List<SrmDeliveryNoteDetDto> findList(Map<String, Object> map) {
        return srmDeliveryNoteDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<SrmDeliveryNoteDet> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        for (SrmDeliveryNoteDet srmDeliveryNoteDet : list) {
            srmDeliveryNoteDet.setModifiedTime(new Date());
            srmDeliveryNoteDet.setModifiedUserId(user.getUserId());
        }

        int i = srmDeliveryNoteDetMapper.batchUpdate(list);

        return i;
    }


}
