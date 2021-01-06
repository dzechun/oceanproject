package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsFirstInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsDisqualification;
import com.fantechs.common.base.general.entity.qms.QmsFirstInspection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsDisqualificationMapper;
import com.fantechs.provider.qms.mapper.QmsFirstInspectionMapper;
import com.fantechs.provider.qms.service.QmsFirstInspectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/06.
 */
@Service
public class QmsFirstInspectionServiceImpl extends BaseService<QmsFirstInspection> implements QmsFirstInspectionService {

    @Resource
    private QmsFirstInspectionMapper qmsFirstInspectionMapper;
    @Resource
    private QmsDisqualificationMapper qmsDisqualificationMapper;

    @Override
    public List<QmsFirstInspectionDto> findList(Map<String, Object> map) {
        return qmsFirstInspectionMapper.findList (map);
    }

    @Override
    public int save(QmsFirstInspection qmsFirstInspection) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsFirstInspection.setCreateTime(new Date());
        qmsFirstInspection.setCreateUserId(user.getUserId());
        qmsFirstInspection.setModifiedTime(new Date());
        qmsFirstInspection.setModifiedUserId(user.getUserId());
        qmsFirstInspection.setStatus(StringUtils.isEmpty(qmsFirstInspection.getStatus())?1:qmsFirstInspection.getStatus());

        int i = qmsFirstInspectionMapper.insert(qmsFirstInspection);

        List<QmsDisqualification> list = qmsFirstInspection.getList();
        for (QmsDisqualification qmsDisqualification : list) {
            qmsDisqualification.setFirstInspectionIdId(qmsFirstInspection.getFirstInspectionId());
        }
        qmsDisqualificationMapper.insertList(list);
        return i;
    }
}
