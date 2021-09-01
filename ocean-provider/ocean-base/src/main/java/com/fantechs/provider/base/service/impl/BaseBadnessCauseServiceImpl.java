package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessCauseDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBadnessCauseMapper;
import com.fantechs.provider.base.mapper.BaseHtBadnessCauseMapper;
import com.fantechs.provider.base.service.BaseBadnessCauseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class BaseBadnessCauseServiceImpl extends BaseService<BaseBadnessCause> implements BaseBadnessCauseService {

    @Resource
    private BaseBadnessCauseMapper baseBadnessCauseMapper;
    @Resource
    private BaseHtBadnessCauseMapper baseHtBadnessCauseMapper;

    @Override
    public List<BaseBadnessCauseDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());
        return baseBadnessCauseMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessCause baseBadnessCause) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseBadnessCause.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("badnessCauseCode",baseBadnessCause.getBadnessCauseCode());
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        BaseBadnessCause baseBadnessCause1 = baseBadnessCauseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCause1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseBadnessCause.setCreateTime(new Date());
        baseBadnessCause.setCreateUserId(currentUser.getUserId());
        baseBadnessCause.setModifiedTime(new Date());
        baseBadnessCause.setModifiedUserId(currentUser.getUserId());
        baseBadnessCause.setStatus((byte) 1);
        baseBadnessCause.setOrgId(currentUser.getOrganizationId());
        int i = baseBadnessCauseMapper.insertUseGeneratedKeys(baseBadnessCause);

        BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
        BeanUtils.copyProperties(baseBadnessCause,baseHtBadnessCause);
        baseHtBadnessCause.setModifiedTime(new Date());
        baseHtBadnessCause.setModifiedUserId(currentUser.getUserId());
        baseHtBadnessCauseMapper.insertSelective(baseHtBadnessCause);

        return i;
    }

    @Override
    public int update(BaseBadnessCause baseBadnessCause) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseBadnessCause.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("badnessCauseCode",baseBadnessCause.getBadnessCauseCode())
                .andNotEqualTo("badnessCauseId",baseBadnessCause.getBadnessCauseId());
        BaseBadnessCause baseBadnessCause1 = baseBadnessCauseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCause1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseBadnessCause.setModifiedTime(new Date());
        baseBadnessCause.setModifiedUserId(currentUser.getUserId());
        baseBadnessCause.setOrgId(currentUser.getOrganizationId());
        int i = baseBadnessCauseMapper.updateByPrimaryKeySelective(baseBadnessCause);

        BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
        BeanUtils.copyProperties(baseBadnessCause,baseHtBadnessCause);
        baseHtBadnessCauseMapper.insertSelective(baseHtBadnessCause);

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        LinkedList<BaseHtBadnessCause> htList = new LinkedList<>();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BaseBadnessCause baseBadnessCause = baseBadnessCauseMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseBadnessCause)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
            BeanUtils.copyProperties(baseBadnessCause,baseHtBadnessCause);
            htList.add(baseHtBadnessCause);
        }

        baseHtBadnessCauseMapper.insertList(htList);
        return baseBadnessCauseMapper.deleteByIds(ids);
    }

    @Override
    public int saveByApi(BaseBadnessCause baseBadnessCause) {
        Example example = new Example(BaseBadnessCause.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("badnessCauseCode", baseBadnessCause.getBadnessCauseCode());
        criteria.andEqualTo("orgId", baseBadnessCause.getOrgId());
       // baseBadnessCauseMapper.deleteByExample(example);
        BaseBadnessCause cause = baseBadnessCauseMapper.selectOneByExample(example);
        int i = 0;
        if(StringUtils.isEmpty(cause)){
            baseBadnessCause.setCreateTime(new Date());
            i =  baseBadnessCauseMapper.insertSelective(baseBadnessCause);
        }else{
            baseBadnessCause.setBadnessCauseId(cause.getBadnessCauseId());
            i =  baseBadnessCauseMapper.updateByPrimaryKeySelective(baseBadnessCause);
        }
        return  i;
    }

}
