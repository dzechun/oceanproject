package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseConsigneeDto;
import com.fantechs.common.base.general.entity.basic.BaseConsignee;
import com.fantechs.common.base.general.entity.basic.history.BaseHtConsignee;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseConsigneeMapper;
import com.fantechs.provider.base.mapper.BaseHtConsigneeMapper;
import com.fantechs.provider.base.service.BaseConsigneeService;
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
 * Created by leifengzhi on 2021/04/23.
 */
@Service
public class BaseConsigneeServiceImpl extends BaseService<BaseConsignee> implements BaseConsigneeService {

    @Resource
    private BaseConsigneeMapper baseConsigneeMapper;

    @Resource
    private BaseHtConsigneeMapper baseHtConsigneeMapper;

    @Override
    public List<BaseConsigneeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseConsigneeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseConsignee baseConsignee) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseConsignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("consigneeCode", baseConsignee.getConsigneeCode());
        BaseConsignee baseConsignee1 = baseConsigneeMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseConsignee1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseConsignee.setCreateUserId(currentUser.getUserId());
        baseConsignee.setCreateTime(new Date());
        baseConsignee.setModifiedUserId(currentUser.getUserId());
        baseConsignee.setModifiedTime(new Date());
        baseConsignee.setStatus(StringUtils.isEmpty(baseConsignee.getStatus())?1:baseConsignee.getStatus());
        baseConsignee.setOrgId(currentUser.getOrganizationId());
        int i = baseConsigneeMapper.insertUseGeneratedKeys(baseConsignee);

        //新增收货人履历
        BaseHtConsignee baseHtConsignee =new BaseHtConsignee();
        BeanUtils.copyProperties(baseConsignee, baseHtConsignee);
        baseHtConsigneeMapper.insertSelective(baseHtConsignee);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseConsignee baseConsignee) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseConsignee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("consigneeCode", baseConsignee.getConsigneeCode())
                .andNotEqualTo("consigneeId",baseConsignee.getConsigneeId());
        BaseConsignee baseConsignee1 = baseConsigneeMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseConsignee1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseConsignee.setModifiedUserId(currentUser.getUserId());
        baseConsignee.setModifiedTime(new Date());
        baseConsignee.setOrgId(currentUser.getOrganizationId());
        int i=baseConsigneeMapper.updateByPrimaryKeySelective(baseConsignee);

        BaseHtConsignee baseHtConsignee = new BaseHtConsignee();
        BeanUtils.copyProperties(baseConsignee, baseHtConsignee);
        baseHtConsigneeMapper.insertSelective(baseHtConsignee);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtConsignee> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseConsignee baseConsignee = baseConsigneeMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseConsignee)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtConsignee baseHtConsignee = new BaseHtConsignee();
            BeanUtils.copyProperties(baseConsignee, baseHtConsignee);
            list.add(baseHtConsignee);
        }
        baseHtConsigneeMapper.insertList(list);

        return baseConsigneeMapper.deleteByIds(ids);
    }
}
