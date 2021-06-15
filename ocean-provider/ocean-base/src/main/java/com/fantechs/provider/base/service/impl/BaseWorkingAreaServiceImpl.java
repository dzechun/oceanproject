package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkingArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkingAreaMapper;
import com.fantechs.provider.base.mapper.BaseWorkingAreaMapper;
import com.fantechs.provider.base.service.BaseWorkingAreaService;
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
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseWorkingAreaServiceImpl extends BaseService<BaseWorkingArea> implements BaseWorkingAreaService {

    @Resource
    private BaseWorkingAreaMapper baseWorkingAreaMapper;

    @Resource
    private BaseHtWorkingAreaMapper baseHtWorkingAreaMapper;

    @Override
    public List<BaseWorkingAreaDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseWorkingAreaMapper.findList(map);
    }

    @Override
    public int save(BaseWorkingArea record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(record.getStatus() == null ? 1 : record.getStatus());

        int i = baseWorkingAreaMapper.insertUseGeneratedKeys(record);

        BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
        BeanUtils.copyProperties(record,baseHtWorkingArea);
        baseHtWorkingAreaMapper.insert(baseHtWorkingArea);

        return i;
    }

    @Override
    public int update(BaseWorkingArea entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        entity.setStatus(entity.getStatus() == null ? 1 : entity.getStatus());
        BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
        BeanUtils.copyProperties(entity,baseHtWorkingArea);
        baseHtWorkingAreaMapper.insert(baseHtWorkingArea);

        return baseWorkingAreaMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtWorkingArea> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseWorkingArea baseWorkingArea = baseWorkingAreaMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseWorkingArea)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
            BeanUtils.copyProperties(baseWorkingArea,baseHtWorkingArea);
            list.add(baseHtWorkingArea);
        }

        baseHtWorkingAreaMapper.insertList(list);
        return baseWorkingAreaMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseWorkingArea entity){
        Example example = new Example(BaseWorkingArea.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("workingAreaCode",entity.getWorkingAreaCode());
        if (StringUtils.isNotEmpty(entity.getWorkingAreaId())){
            criteria.andNotEqualTo("workingAreaId",entity.getWorkingAreaId());
        }
        BaseWorkingArea baseBadnessCategory = baseWorkingAreaMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }
}
