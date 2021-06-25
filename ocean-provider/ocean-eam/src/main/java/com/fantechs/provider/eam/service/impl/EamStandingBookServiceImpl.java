package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamMaintainProject;
import com.fantechs.common.base.general.entity.eam.EamStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtStandingBook;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtStandingBookMapper;
import com.fantechs.provider.eam.mapper.EamStandingBookMapper;
import com.fantechs.provider.eam.service.EamStandingBookService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamStandingBookServiceImpl extends BaseService<EamStandingBook> implements EamStandingBookService {

    @Resource
    private EamStandingBookMapper eamStandingBookMapper;
    @Resource
    private EamHtStandingBookMapper eamHtStandingBookMapper;

    @Override
    public List<EamStandingBookDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamStandingBookMapper.findList(map);
    }

    @Override
    public List<EamHtStandingBook> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamHtStandingBookMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamStandingBook record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = eamStandingBookMapper.insertUseGeneratedKeys(record);

        EamHtStandingBook eamHtStandingBook = new EamHtStandingBook();
        BeanUtils.copyProperties(record, eamHtStandingBook);
        eamHtStandingBookMapper.insert(eamHtStandingBook);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamStandingBook entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtStandingBook eamHtStandingBook = new EamHtStandingBook();
        BeanUtils.copyProperties(entity, eamHtStandingBook);
        eamHtStandingBookMapper.insert(eamHtStandingBook);

        return eamStandingBookMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtStandingBook> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamStandingBook eamStandingBook = eamStandingBookMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamStandingBook)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtStandingBook eamHtStandingBook = new EamHtStandingBook();
            BeanUtils.copyProperties(eamStandingBook, eamHtStandingBook);
            list.add(eamHtStandingBook);
        }

        eamHtStandingBookMapper.insertList(list);

        return eamStandingBookMapper.deleteByIds(ids);
    }
}
