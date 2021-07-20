package com.fantechs.provider.smt.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.smt.SmtMaterialLifetimeDto;
import com.fantechs.common.base.general.entity.smt.SmtMaterialLifetime;
import com.fantechs.common.base.general.entity.smt.history.SmtHtMaterialLifetime;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.smt.mapper.SmtHtMaterialLifetimeMapper;
import com.fantechs.provider.smt.mapper.SmtMaterialLifetimeMapper;
import com.fantechs.provider.smt.service.SmtMaterialLifetimeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/20.
 */
@Service
public class SmtMaterialLifetimeServiceImpl extends BaseService<SmtMaterialLifetime> implements SmtMaterialLifetimeService {

    @Resource
    private SmtMaterialLifetimeMapper smtMaterialLifetimeMapper;
    @Resource
    private SmtHtMaterialLifetimeMapper smtHtMaterialLifetimeMapper;

    @Override
    public List<SmtMaterialLifetimeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return smtMaterialLifetimeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SmtMaterialLifetime record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        smtMaterialLifetimeMapper.insertUseGeneratedKeys(record);

        SmtHtMaterialLifetime smtHtMaterialLifetime = new SmtHtMaterialLifetime();
        BeanUtils.copyProperties(record, smtHtMaterialLifetime);
        int i = smtHtMaterialLifetimeMapper.insert(smtHtMaterialLifetime);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(SmtMaterialLifetime entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        SmtHtMaterialLifetime smtHtMaterialLifetime = new SmtHtMaterialLifetime();
        BeanUtils.copyProperties(entity, smtHtMaterialLifetime);
        smtHtMaterialLifetimeMapper.insert(smtHtMaterialLifetime);

        return smtMaterialLifetimeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<SmtHtMaterialLifetime> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            SmtMaterialLifetime smtMaterialLifetime = smtMaterialLifetimeMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtMaterialLifetime)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            SmtHtMaterialLifetime smtHtMaterialLifetime = new SmtHtMaterialLifetime();
            BeanUtils.copyProperties(smtMaterialLifetime, smtHtMaterialLifetime);
            list.add(smtHtMaterialLifetime);
        }

        smtHtMaterialLifetimeMapper.insertList(list);

        return smtMaterialLifetimeMapper.deleteByIds(ids);
    }
}
