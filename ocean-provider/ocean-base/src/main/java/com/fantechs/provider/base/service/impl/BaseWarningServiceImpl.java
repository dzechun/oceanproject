package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.entity.basic.BaseWarning;
import com.fantechs.common.base.general.entity.basic.BaseWarningPersonnel;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarning;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarningPersonnel;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWarningMapper;
import com.fantechs.provider.base.mapper.BaseWarningMapper;
import com.fantechs.provider.base.mapper.BaseWarningPersonnelMapper;
import com.fantechs.provider.base.service.BaseWarningService;
import io.micrometer.core.instrument.search.Search;
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
 * Created by leifengzhi on 2021/03/03.
 */
@Service
public class BaseWarningServiceImpl extends BaseService<BaseWarning> implements BaseWarningService {

    @Resource
    private BaseWarningMapper baseWarningMapper;
    @Resource
    private BaseHtWarningMapper baseHtWarningMapper;
    @Resource
    private BaseWarningPersonnelMapper baseWarningPersonnelMapper;

    @Override
    public List<BaseWarningDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseWarningDto> baseWarningDtos = baseWarningMapper.findList(map);

        for (BaseWarningDto baseWarningDto : baseWarningDtos) {
            SearchBaseWarningPersonnel searchBaseWarningPersonnel = new SearchBaseWarningPersonnel();
            searchBaseWarningPersonnel.setWarningId(baseWarningDto.getWarningId());
            List<BaseWarningPersonnelDto> baseWarningPersonnels = baseWarningPersonnelMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarningPersonnel));
            baseWarningDto.setBaseWarningPersonnelDtoList(baseWarningPersonnels);
        }

        return baseWarningDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWarning baseWarning) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWarning.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("warningType",baseWarning.getWarningType())
                .andEqualTo("personnelLevel",baseWarning.getPersonnelLevel());
        BaseWarning baseWarning1 = baseWarningMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWarning1)){
            throw new BizErrorException("预警类型和人员等级的关系已存在");
        }

        //新增预警信息
        baseWarning.setCreateTime(new Date());
        baseWarning.setCreateUserId(user.getUserId());
        baseWarning.setModifiedTime(new Date());
        baseWarning.setModifiedUserId(user.getUserId());
        baseWarning.setOrganizationId(user.getOrganizationId());
        int i = baseWarningMapper.insertUseGeneratedKeys(baseWarning);

        //新增预警信息履历
       /* BaseHtWarning baseHtWarning = new BaseHtWarning();
        BeanUtils.copyProperties(baseWarning,baseHtWarning);
        baseHtWarningMapper.insertSelective(baseHtWarning);*/

        //新增预警人员信息
        List<BaseWarningPersonnelDto> baseWarningPersonnelDtoList = baseWarning.getBaseWarningPersonnelDtoList();
        if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)){
            for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningPersonnelDtoList) {
                baseWarningPersonnelDto.setWarningId(baseWarning.getWarningId());
            }
        }
        if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)){
            baseWarningPersonnelMapper.insertList(baseWarningPersonnelDtoList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWarning baseWarning) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWarning.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("warningType",baseWarning.getWarningType())
                .andEqualTo("personnelLevel",baseWarning.getPersonnelLevel())
                .andNotEqualTo("warningId",baseWarning.getWarningId());
        BaseWarning baseWarning1 = baseWarningMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWarning1)){
            throw new BizErrorException("预警类型和人员等级的关系已存在");
        }

        //更新预警信息
        baseWarning.setModifiedUserId(user.getUserId());
        baseWarning.setModifiedTime(new Date());
        baseWarning.setOrganizationId(user.getOrganizationId());
        int i = baseWarningMapper.updateByPrimaryKeySelective(baseWarning);

        //新增履历
       /* BaseHtWarning baseHtWarning = new BaseHtWarning();
        BeanUtils.copyProperties(baseWarning,baseHtWarning);
        baseHtWarningMapper.insertSelective(baseHtWarning);*/

        //更新预警人员信息
        //删除原有绑定关系
        Example example1 = new Example(BaseWarningPersonnel.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("warningId",baseWarning.getWarningId());
        baseWarningPersonnelMapper.deleteByExample(example1);
        //新增绑定关系
        List<BaseWarningPersonnelDto> baseWarningPersonnelDtoList = baseWarning.getBaseWarningPersonnelDtoList();
        if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)){
            for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningPersonnelDtoList) {
                baseWarningPersonnelDto.setWarningId(baseWarning.getWarningId());
            }
        }
        if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)){
            baseWarningPersonnelMapper.insertList(baseWarningPersonnelDtoList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        ArrayList<BaseHtWarning> baseHtWarnings = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseWarning baseWarning = baseWarningMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseWarning)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtWarning baseHtWarning = new BaseHtWarning();
            BeanUtils.copyProperties(baseWarning,baseHtWarning);
            baseHtWarnings.add(baseHtWarning);

            //删除绑定关系
            Example example = new Example(BaseWarningPersonnel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warningId",baseWarning.getWarningId());
            baseWarningPersonnelMapper.deleteByExample(example);
        }

        //baseHtWarningMapper.insertList(baseHtWarnings);
        return baseWarningMapper.deleteByIds(ids);
    }
}
