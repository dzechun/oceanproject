package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePartsInformationDto;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionItem;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPartsInformationMapper;
import com.fantechs.provider.base.mapper.BasePartsInformationMapper;
import com.fantechs.provider.base.service.BasePartsInformationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/14.
 */
@Service
public class BasePartsInformationServiceImpl  extends BaseService<BasePartsInformation> implements BasePartsInformationService {

    @Resource
    private BasePartsInformationMapper basePartsInformationMapper;
    @Resource
    private BaseHtPartsInformationMapper baseHtPartsInformationMapper;

    @Override
    public List<BasePartsInformationDto> findList(Map<String, Object> map) {
        return basePartsInformationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BasePartsInformation basePartsInformation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        basePartsInformation.setCreateTime(new Date());
        basePartsInformation.setCreateUserId(user.getUserId());
        basePartsInformation.setModifiedTime(new Date());
        basePartsInformation.setModifiedUserId(user.getUserId());
        basePartsInformation.setStatus(StringUtils.isEmpty(basePartsInformation.getStatus())?1:basePartsInformation.getStatus());
        basePartsInformation.setOrganizationId(user.getOrganizationId());
        basePartsInformation.setPartsInformationCode(CodeUtils.getId("ZCB"));
        int i = basePartsInformationMapper.insertUseGeneratedKeys(basePartsInformation);

        BaseHtPartsInformation baseHtPartsInformation = new BaseHtPartsInformation();
        BeanUtils.copyProperties(basePartsInformation,baseHtPartsInformation);
        baseHtPartsInformationMapper.insert(baseHtPartsInformation);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BasePartsInformation basePartsInformation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        basePartsInformation.setModifiedTime(new Date());
        basePartsInformation.setModifiedUserId(user.getUserId());

        BaseHtPartsInformation baseHtPartsInformation = new BaseHtPartsInformation();
        BeanUtils.copyProperties(basePartsInformation,baseHtPartsInformation);
        baseHtPartsInformationMapper.insert(baseHtPartsInformation);

        return basePartsInformationMapper.updateByPrimaryKeySelective(basePartsInformation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtPartsInformation> baseHtPartsInformations = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BasePartsInformation basePartsInformation = basePartsInformationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(basePartsInformation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtPartsInformation qmsHtInspectionType = new BaseHtPartsInformation();
            BeanUtils.copyProperties(basePartsInformation,qmsHtInspectionType);
            baseHtPartsInformations.add(qmsHtInspectionType);
        }

        baseHtPartsInformationMapper.insertList(baseHtPartsInformations);

        return basePartsInformationMapper.deleteByIds(ids);
    }
}
