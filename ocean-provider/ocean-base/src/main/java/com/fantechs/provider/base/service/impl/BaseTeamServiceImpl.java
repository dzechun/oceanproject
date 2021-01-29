package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.search.SearchSmtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.base.mapper.BaseHtTeamMapper;
import com.fantechs.provider.base.mapper.BaseTeamMapper;
import com.fantechs.provider.base.service.BaseTeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

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
    @Resource
    private BasicFeignApi basicFeignApi;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseTeamDto> baseTeamDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseTeam> list = new LinkedList<>();
        LinkedList<BaseHtTeam> htList = new LinkedList<>();
        for (int i = 0; i < baseTeamDtos.size(); i++) {
            BaseTeamDto baseTeamDto = baseTeamDtos.get(i);
            String factoryCode = baseTeamDto.getFactoryCode();
            String teamCode = baseTeamDto.getTeamCode();
            String teamName = baseTeamDto.getTeamName();
            if (StringUtils.isEmpty(
                    factoryCode,teamCode,teamName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("teamCode",baseTeamDto.getTeamCode());
            if (StringUtils.isNotEmpty(baseTeamMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            //判断工厂是否存在
            SearchSmtFactory searchSmtFactory = new SearchSmtFactory();
            searchSmtFactory.setFactoryCode(baseTeamDto.getFactoryCode());
            searchSmtFactory.setCodeQueryMark((byte) 1);
            SmtFactoryDto smtFactoryDto = basicFeignApi.findFactoryList(searchSmtFactory).getData().get(0);
            if (StringUtils.isEmpty(smtFactoryDto)){
                fail.add(i+3);
                continue;
            }

            BaseTeam baseTeam = new BaseTeam();
            BeanUtils.copyProperties(baseTeamDto,baseTeam);
            baseTeam.setCreateTime(new Date());
            baseTeam.setCreateUserId(currentUser.getUserId());
            baseTeam.setModifiedTime(new Date());
            baseTeam.setModifiedUserId(currentUser.getUserId());
            baseTeam.setStatus((byte) 1);
            list.add(baseTeam);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseTeamMapper.insertList(list);
        }

        for (BaseTeam baseTeam : list) {
            BaseHtTeam baseHtTeam = new BaseHtTeam();
            BeanUtils.copyProperties(baseTeam,baseHtTeam);
            htList.add(baseHtTeam);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtTeamMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
