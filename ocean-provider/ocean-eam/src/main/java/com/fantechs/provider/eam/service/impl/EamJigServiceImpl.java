package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigMapper;
import com.fantechs.provider.eam.mapper.EamJigMapper;
import com.fantechs.provider.eam.service.EamJigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigServiceImpl extends BaseService<EamJig> implements EamJigService {

    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private EamHtJigMapper eamHtJigMapper;

    @Override
    public List<EamJigDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJig record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigCode", record.getJigCode())
                .orEqualTo("jigName",record.getJigName());
        EamJig eamJig = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamJigMapper.insertUseGeneratedKeys(record);

        EamHtJig eamHtJig = new EamHtJig();
        BeanUtils.copyProperties(record, eamHtJig);
        int i = eamHtJigMapper.insert(eamHtJig);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJig entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigCode", entity.getJigCode())
                .orEqualTo("jigName",entity.getJigName());
        EamJig eamJig = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJig)&&!entity.getJigId().equals(eamJig.getJigId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtJig eamHtJig = new EamHtJig();
        BeanUtils.copyProperties(entity, eamHtJig);
        eamHtJigMapper.insert(eamHtJig);

        return eamJigMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJig> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamJig eamJig = eamJigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamJig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJig eamHtJig = new EamHtJig();
            BeanUtils.copyProperties(eamJig, eamHtJig);
            list.add(eamHtJig);
        }

        eamHtJigMapper.insertList(list);

        return eamJigMapper.deleteByIds(ids);
    }
}
