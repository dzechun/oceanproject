package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtTeamMapper;
import com.fantechs.provider.base.mapper.BaseTeamMapper;
import com.fantechs.provider.base.service.BaseTeamService;
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
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BaseTeamServiceImpl  extends BaseService<BaseTeam> implements BaseTeamService {

    @Resource
    private BaseTeamMapper baseTeamMapper;
    @Resource
    private BaseHtTeamMapper baseHtTeamMapper;

    @Override
    public List<BaseTeamDto> findList(Map<String, Object> map) {
        return baseTeamMapper.findList(map);
    }

    @Override
    public int save(BaseTeam baseTeam) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseTeam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("teamCode",baseTeam.getTeamCode());
        BaseTeam baseTeam1 = baseTeamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseTeam1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseTeam.setCreateTime(new Date());
        baseTeam.setCreateUserId(user.getUserId());
        baseTeam.setModifiedTime(new Date());
        baseTeam.setModifiedUserId(user.getUserId());
        baseTeam.setOrganizationId(user.getOrganizationId());
        int i = baseTeamMapper.insertUseGeneratedKeys(baseTeam);

        BaseHtTeam baseHtTeam = new BaseHtTeam();
        BeanUtils.copyProperties(baseTeam,baseHtTeam);
        baseHtTeamMapper.insertSelective(baseHtTeam);
        return i;
    }

    @Override
    public int update(BaseTeam baseTeam) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseTeam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("teamCode",baseTeam.getTeamCode())
                .andNotEqualTo("teamId",baseTeam.getTeamId());
        BaseTeam baseTeam1 = baseTeamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseTeam1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseTeam.setModifiedTime(new Date());
        baseTeam.setModifiedUserId(user.getUserId());
        baseTeam.setOrganizationId(user.getOrganizationId());

        BaseHtTeam baseHtTeam = new BaseHtTeam();
        BeanUtils.copyProperties(baseTeam,baseHtTeam);
        baseHtTeamMapper.insertSelective(baseHtTeam);

        return baseTeamMapper.updateByPrimaryKeySelective(baseTeam);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtTeam> baseHtTeams = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseTeam baseTeam = baseTeamMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseTeam)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtTeam baseHtTeam = new BaseHtTeam();
            BeanUtils.copyProperties(baseTeam,baseHtTeam);
            baseHtTeams.add(baseHtTeam);
        }

        baseHtTeamMapper.insertList(baseHtTeams);
        return baseTeamMapper.deleteByIds(ids);
    }
}
