package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.history.BaseHtOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtOrganizationMapper;
import com.fantechs.provider.base.mapper.BaseOrganizationMapper;
import com.fantechs.provider.base.service.BaseOrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class BaseOrganizationServiceImpl extends BaseService<BaseOrganization> implements BaseOrganizationService {

    @Resource
    private BaseOrganizationMapper baseOrganizationMapper;
    @Resource
    private BaseHtOrganizationMapper baseHtOrganizationMapper;


    @Override
    public int save(BaseOrganization baseOrganization) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrganization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationCode",baseOrganization.getOrganizationCode());
        BaseOrganization baseOrganization1 = baseOrganizationMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseOrganization1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseOrganization.setCreateUserId(user.getUserId());
        baseOrganization.setCreateTime(new Date());
        baseOrganization.setModifiedUserId(user.getUserId());
        baseOrganization.setModifiedTime(new Date());
        baseOrganization.setStatus(StringUtils.isEmpty(baseOrganization.getStatus())?1:baseOrganization.getStatus());
        int i = baseOrganizationMapper.insertUseGeneratedKeys(baseOrganization);

        BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
        BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
        baseHtOrganizationMapper.insertSelective(baseHtOrganization);

        return i;
    }

    @Override
    public int update(BaseOrganization baseOrganization) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseOrganization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationCode",baseOrganization.getOrganizationCode())
                .andNotEqualTo("organizationId",baseOrganization.getOrganizationId());
        BaseOrganization baseOrganization1 = baseOrganizationMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseOrganization1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseOrganization.setModifiedTime(new Date());
        baseOrganization.setModifiedUserId(user.getUserId());

        BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
        BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
        baseHtOrganizationMapper.insertSelective(baseHtOrganization);

        return baseOrganizationMapper.updateByPrimaryKeySelective(baseOrganization);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtOrganization> baseHtOrganizations = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseOrganization baseOrganization = baseOrganizationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseOrganization)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
            BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
            baseHtOrganizations.add(baseHtOrganization);
        }

        baseHtOrganizationMapper.insertList(baseHtOrganizations);
        return baseOrganizationMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseOrganizationDto> findList(Map<String, Object> map) {
        Object userId = map.get("userId");
        if (StringUtils.isNotEmpty(userId)){
            return baseOrganizationMapper.findOrganizationByUserId(new Long(userId.toString()));
        }

        return baseOrganizationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUser(Long organizationId, List<Long> userIds) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        baseOrganizationMapper.deleteUserByOrganization(organizationId);

        List<SysOrganizationUser> list = new ArrayList<>();
        for (Long userId : userIds) {
            SysOrganizationUser sysOrganizationUser = new SysOrganizationUser();
            sysOrganizationUser.setOrganizationId(organizationId);
            sysOrganizationUser.setUserId(userId);
            list.add(sysOrganizationUser);
        }
        int i = 1;
        if (StringUtils.isNotEmpty(list)){
            i = baseOrganizationMapper.insertUser(list);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseOrganizationDto> baseOrganizationDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseOrganization> list = new LinkedList<>();
        LinkedList<BaseHtOrganization> htList = new LinkedList<>();
        for (int i = 0; i < baseOrganizationDtos.size(); i++) {
            BaseOrganizationDto baseOrganizationDto = baseOrganizationDtos.get(i);
            String organizationCode = baseOrganizationDto.getOrganizationCode();
            String organizationName = baseOrganizationDto.getOrganizationName();
            if (StringUtils.isEmpty(
                    organizationCode,organizationName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationCode",baseOrganizationDto.getOrganizationCode());
            if (StringUtils.isNotEmpty(baseOrganizationMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            BaseOrganization baseOrganization = new BaseOrganization();
            BeanUtils.copyProperties(baseOrganizationDto,baseOrganization);
            baseOrganization.setCreateTime(new Date());
            baseOrganization.setCreateUserId(currentUser.getUserId());
            baseOrganization.setModifiedTime(new Date());
            baseOrganization.setModifiedUserId(currentUser.getUserId());
            baseOrganization.setStatus((byte) 1);
            list.add(baseOrganization);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseOrganizationMapper.insertList(list);
        }
        for (BaseOrganization baseOrganization : list) {
            BaseHtOrganization baseHtOrganization = new BaseHtOrganization();
            BeanUtils.copyProperties(baseOrganization,baseHtOrganization);
            htList.add(baseHtOrganization);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtOrganizationMapper.insertList(htList);
        }
        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
