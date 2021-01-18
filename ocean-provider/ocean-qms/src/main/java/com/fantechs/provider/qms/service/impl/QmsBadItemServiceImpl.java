package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItem;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsBadItemMapper;
import com.fantechs.provider.qms.service.QmsBadItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/16.
 */
@Service
public class QmsBadItemServiceImpl extends BaseService<QmsBadItem> implements QmsBadItemService {

    @Resource
    private QmsBadItemMapper qmsBadItemMapper;

    @Override
    public List<QmsBadItemDto> findList(Map<String, Object> map) {
        return qmsBadItemMapper.findList(map);
    }

    @Override
    public int save(QmsBadItem qmsBadItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsBadItem.setCreateTime(new Date());
        qmsBadItem.setCreateUserId(user.getUserId());
        qmsBadItem.setModifiedTime(new Date());
        qmsBadItem.setModifiedUserId(user.getUserId());
        qmsBadItem.setStatus(StringUtils.isEmpty(qmsBadItem.getStatus())?1:qmsBadItem.getStatus());
        qmsBadItem.setOrganizationId(user.getOrganizationId());
        qmsBadItem.setBadItemCode(CodeUtils.getId("QBI"));

        return qmsBadItemMapper.insert(qmsBadItem);
    }
}
