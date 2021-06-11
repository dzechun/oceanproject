package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProducttionKeyIssues;
import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProducttionKeyIssuesMapper;
import com.fantechs.provider.base.mapper.BaseProducttionKeyIssuesDetMapper;
import com.fantechs.provider.base.mapper.BaseProducttionKeyIssuesMapper;
import com.fantechs.provider.base.service.BaseProducttionKeyIssuesService;
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
 * Created by leifengzhi on 2021/06/10.
 */
@Service
public class BaseProducttionKeyIssuesServiceImpl extends BaseService<BaseProducttionKeyIssues> implements BaseProducttionKeyIssuesService {

    @Resource
    private BaseProducttionKeyIssuesMapper baseProducttionKeyIssuesMapper;
    @Resource
    private BaseHtProducttionKeyIssuesMapper baseHtProducttionKeyIssuesMapper;
    @Resource
    private BaseProducttionKeyIssuesDetMapper baseProducttionKeyIssuesDetMapper;

    @Override
    public List<BaseProducttionKeyIssues> findList(Map<String, Object> map) {
        return baseProducttionKeyIssuesMapper.findList(map);
    }

    @Override
    public int save(BaseProducttionKeyIssues baseProducttionKeyIssues) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断是否重复
        Example example = new Example(BaseProducttionKeyIssues.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseProducttionKeyIssues.getMaterialId());
        List<BaseProducttionKeyIssues> baseProducttionKeyIssuesList = baseProducttionKeyIssuesMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProducttionKeyIssuesList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增单价信息
        baseProducttionKeyIssues.setCreateTime(new Date());
        baseProducttionKeyIssues.setCreateUserId(user.getUserId());
        baseProducttionKeyIssues.setModifiedTime(new Date());
        baseProducttionKeyIssues.setModifiedUserId(user.getUserId());
        baseProducttionKeyIssues.setOrgId(user.getOrganizationId());
        baseProducttionKeyIssues.setStatus(StringUtils.isEmpty(baseProducttionKeyIssues.getStatus())?(byte)1:baseProducttionKeyIssues.getStatus());
        int i = baseProducttionKeyIssuesMapper.insertUseGeneratedKeys(baseProducttionKeyIssues);

        //新增履历
        BaseHtProducttionKeyIssues baseHtProducttionKeyIssues = new BaseHtProducttionKeyIssues();
        BeanUtils.copyProperties(baseProducttionKeyIssues,baseHtProducttionKeyIssues);
        baseHtProducttionKeyIssuesMapper.insertSelective(baseHtProducttionKeyIssues);

        //新增明细
        List<BaseProducttionKeyIssuesDet> baseProducttionKeyIssuesDetList = baseProducttionKeyIssues.getBaseProducttionKeyIssuesDetList();
        if (StringUtils.isNotEmpty(baseProducttionKeyIssuesDetList)){
            for (BaseProducttionKeyIssuesDet baseProducttionKeyIssuesDet : baseProducttionKeyIssuesDetList) {
                baseProducttionKeyIssuesDet.setProducttionKeyIssuesId(baseProducttionKeyIssues.getProducttionKeyIssuesId());
                baseProducttionKeyIssuesDet.setCreateTime(new Date());
                baseProducttionKeyIssuesDet.setCreateUserId(user.getUserId());
                baseProducttionKeyIssuesDet.setModifiedTime(new Date());
                baseProducttionKeyIssuesDet.setModifiedUserId(user.getUserId());
                baseProducttionKeyIssuesDet.setOrgId(user.getOrganizationId());
                baseProducttionKeyIssuesDet.setStatus(StringUtils.isEmpty(baseProducttionKeyIssuesDet.getStatus())?(byte)1:baseProducttionKeyIssuesDet.getStatus());
            }
            baseProducttionKeyIssuesDetMapper.insertList(baseProducttionKeyIssuesDetList);
        }

        return i;
    }

    @Override
    public int update(BaseProducttionKeyIssues baseProducttionKeyIssues) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断是否重复
        Example example = new Example(BaseProducttionKeyIssues.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseProducttionKeyIssues.getMaterialId())
                .andNotEqualTo("producttionKeyIssuesId",baseProducttionKeyIssues.getProducttionKeyIssuesId());
        List<BaseProducttionKeyIssues> baseProducttionKeyIssuesList = baseProducttionKeyIssuesMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProducttionKeyIssuesList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProducttionKeyIssues.setModifiedTime(new Date());
        baseProducttionKeyIssues.setModifiedUserId(user.getUserId());
        baseProducttionKeyIssues.setOrgId(user.getOrganizationId());
        int i = baseProducttionKeyIssuesMapper.updateByPrimaryKeySelective(baseProducttionKeyIssues);

        //新增履历
        BaseHtProducttionKeyIssues baseHtProducttionKeyIssues = new BaseHtProducttionKeyIssues();
        BeanUtils.copyProperties(baseProducttionKeyIssues,baseHtProducttionKeyIssues);
        baseHtProducttionKeyIssuesMapper.insertSelective(baseHtProducttionKeyIssues);

        //删除原明细
        Example example1 = new Example(BaseProducttionKeyIssuesDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("producttionKeyIssuesId",baseProducttionKeyIssues.getProducttionKeyIssuesId());
        baseProducttionKeyIssuesDetMapper.deleteByExample(example1);

        //新增明细
        List<BaseProducttionKeyIssuesDet> baseProducttionKeyIssuesDetList = baseProducttionKeyIssues.getBaseProducttionKeyIssuesDetList();
        if (StringUtils.isNotEmpty(baseProducttionKeyIssuesDetList)){
            for (BaseProducttionKeyIssuesDet baseProducttionKeyIssuesDet : baseProducttionKeyIssuesDetList) {
                baseProducttionKeyIssuesDet.setProducttionKeyIssuesId(baseProducttionKeyIssues.getProducttionKeyIssuesId());
                baseProducttionKeyIssuesDet.setCreateTime(new Date());
                baseProducttionKeyIssuesDet.setCreateUserId(user.getUserId());
                baseProducttionKeyIssuesDet.setModifiedTime(new Date());
                baseProducttionKeyIssuesDet.setModifiedUserId(user.getUserId());
                baseProducttionKeyIssuesDet.setOrgId(user.getOrganizationId());
                baseProducttionKeyIssuesDet.setStatus(StringUtils.isEmpty(baseProducttionKeyIssuesDet.getStatus())?(byte)1:baseProducttionKeyIssuesDet.getStatus());
            }
            baseProducttionKeyIssuesDetMapper.insertList(baseProducttionKeyIssuesDetList);
        }

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtProducttionKeyIssues> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseProducttionKeyIssues baseProducttionKeyIssues = baseProducttionKeyIssuesMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseProducttionKeyIssues)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtProducttionKeyIssues baseHtProducttionKeyIssues = new BaseHtProducttionKeyIssues();
            BeanUtils.copyProperties(baseProducttionKeyIssues, baseHtProducttionKeyIssues);
            list.add(baseHtProducttionKeyIssues);

            //删除明细
            Example example1 = new Example(BaseProducttionKeyIssuesDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("producttionKeyIssuesId",baseProducttionKeyIssues.getProducttionKeyIssuesId());
            baseProducttionKeyIssuesDetMapper.deleteByExample(example1);
        }

        baseHtProducttionKeyIssuesMapper.insertList(list);

        return baseProducttionKeyIssuesMapper.deleteByIds(ids);
    }
}
