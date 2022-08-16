package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessDutyDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessDuty;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessDuty;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBadnessDutyMapper;
import com.fantechs.provider.base.mapper.BaseHtBadnessDutyMapper;
import com.fantechs.provider.base.service.BaseBadnessDutyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/08.
 */
@Service
public class BaseBadnessDutyServiceImpl extends BaseService<BaseBadnessDuty> implements BaseBadnessDutyService {

    @Resource
    private BaseBadnessDutyMapper baseBadnessDutyMapper;

    @Resource
    private BaseHtBadnessDutyMapper baseHtBadnessDutyMapper;

    @Override
    public List<BaseBadnessDutyDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseBadnessDutyMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessDuty record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseBadnessDutyMapper.insertUseGeneratedKeys(record);

        BaseHtBadnessDuty baseHtBadnessDuty = new BaseHtBadnessDuty();
        BeanUtils.copyProperties(record,baseHtBadnessDuty);
        baseHtBadnessDutyMapper.insertSelective(baseHtBadnessDuty);

        return i;
    }

    @Override
    public int update(BaseBadnessDuty entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        BaseHtBadnessDuty baseHtBadnessDuty = new BaseHtBadnessDuty();
        BeanUtils.copyProperties(entity,baseHtBadnessDuty);
        baseHtBadnessDutyMapper.insertSelective(baseHtBadnessDuty);

        return baseBadnessDutyMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtBadnessDuty> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseBadnessDuty baseBadnessDuty = baseBadnessDutyMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseBadnessDuty)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtBadnessDuty baseHtBadnessDuty = new BaseHtBadnessDuty();
            BeanUtils.copyProperties(baseBadnessDuty,baseHtBadnessDuty);
            list.add(baseHtBadnessDuty);
        }

        baseHtBadnessDutyMapper.insertList(list);
        return baseBadnessDutyMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseBadnessDuty entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseBadnessDuty.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("badnessDutyCode",entity.getBadnessDutyCode());
        if (StringUtils.isNotEmpty(entity.getBadnessDutyId())){
            criteria.andNotEqualTo("badnessDutyId",entity.getBadnessDutyId());
        }
        BaseBadnessDuty baseBadnessDuty = baseBadnessDutyMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessDuty)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }


}
