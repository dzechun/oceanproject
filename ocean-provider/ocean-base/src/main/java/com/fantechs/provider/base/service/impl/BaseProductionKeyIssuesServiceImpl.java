package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductionKeyIssues;
import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductionKeyIssuesMapper;
import com.fantechs.provider.base.mapper.BaseProductionKeyIssuesDetMapper;
import com.fantechs.provider.base.mapper.BaseProductionKeyIssuesMapper;
import com.fantechs.provider.base.service.BaseProductionKeyIssuesService;
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
 * Created by leifengzhi on 2021/06/10.
 */
@Service
public class BaseProductionKeyIssuesServiceImpl extends BaseService<BaseProductionKeyIssues> implements BaseProductionKeyIssuesService {

    @Resource
    private BaseProductionKeyIssuesMapper baseProductionKeyIssuesMapper;
    @Resource
    private BaseHtProductionKeyIssuesMapper baseHtProductionKeyIssuesMapper;
    @Resource
    private BaseProductionKeyIssuesDetMapper baseProductionKeyIssuesDetMapper;

    @Override
    public List<BaseProductionKeyIssues> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        map.put("orgId", user.getOrganizationId());
        return baseProductionKeyIssuesMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductionKeyIssues baseProductionKeyIssues) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断是否重复
        Example example = new Example(BaseProductionKeyIssues.class);
        Example.Criteria criteria = example.createCriteria();
        List<BaseProductionKeyIssues> baseProductionKeyIssuesList;
        if(StringUtils.isNotEmpty(baseProductionKeyIssues.getMaterialId())) {
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("materialId", baseProductionKeyIssues.getMaterialId());
            baseProductionKeyIssuesList = baseProductionKeyIssuesMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseProductionKeyIssuesList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }else {
            //只能维护一条通用的数据
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("keyIssuesType", 2);
            baseProductionKeyIssuesList = baseProductionKeyIssuesMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseProductionKeyIssuesList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }

        //新增关键事项
        baseProductionKeyIssues.setCreateTime(new Date());
        baseProductionKeyIssues.setCreateUserId(user.getUserId());
        baseProductionKeyIssues.setModifiedTime(new Date());
        baseProductionKeyIssues.setModifiedUserId(user.getUserId());
        baseProductionKeyIssues.setOrgId(user.getOrganizationId());
        baseProductionKeyIssues.setStatus(StringUtils.isEmpty(baseProductionKeyIssues.getStatus())?(byte)1:baseProductionKeyIssues.getStatus());
        int i = baseProductionKeyIssuesMapper.insertUseGeneratedKeys(baseProductionKeyIssues);

        //新增履历
        BaseHtProductionKeyIssues baseHtProductionKeyIssues = new BaseHtProductionKeyIssues();
        BeanUtils.copyProperties(baseProductionKeyIssues,baseHtProductionKeyIssues);
        baseHtProductionKeyIssuesMapper.insertSelective(baseHtProductionKeyIssues);

        //新增明细
        List<BaseProductionKeyIssuesDet> baseProductionKeyIssuesDetList = baseProductionKeyIssues.getBaseProductionKeyIssuesDetList();
        if (StringUtils.isNotEmpty(baseProductionKeyIssuesDetList)){
            for (BaseProductionKeyIssuesDet baseProductionKeyIssuesDet : baseProductionKeyIssuesDetList) {
                baseProductionKeyIssuesDet.setProductionKeyIssuesId(baseProductionKeyIssues.getProductionKeyIssuesId());
                baseProductionKeyIssuesDet.setCreateTime(new Date());
                baseProductionKeyIssuesDet.setCreateUserId(user.getUserId());
                baseProductionKeyIssuesDet.setModifiedTime(new Date());
                baseProductionKeyIssuesDet.setModifiedUserId(user.getUserId());
                baseProductionKeyIssuesDet.setOrgId(user.getOrganizationId());
                baseProductionKeyIssuesDet.setStatus(StringUtils.isEmpty(baseProductionKeyIssuesDet.getStatus())?(byte)1:baseProductionKeyIssuesDet.getStatus());
            }
            baseProductionKeyIssuesDetMapper.insertList(baseProductionKeyIssuesDetList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductionKeyIssues baseProductionKeyIssues) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断是否重复
        Example example = new Example(BaseProductionKeyIssues.class);
        Example.Criteria criteria = example.createCriteria();
        List<BaseProductionKeyIssues> baseProductionKeyIssuesList;
        if(StringUtils.isNotEmpty(baseProductionKeyIssues.getMaterialId())) {
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("materialId", baseProductionKeyIssues.getMaterialId())
                    .andNotEqualTo("productionKeyIssuesId",baseProductionKeyIssues.getProductionKeyIssuesId());
            baseProductionKeyIssuesList = baseProductionKeyIssuesMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseProductionKeyIssuesList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }else {
            //只能维护一条通用的数据
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("keyIssuesType", 2)
                    .andNotEqualTo("productionKeyIssuesId",baseProductionKeyIssues.getProductionKeyIssuesId());
            baseProductionKeyIssuesList = baseProductionKeyIssuesMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseProductionKeyIssuesList)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }

        baseProductionKeyIssues.setModifiedTime(new Date());
        baseProductionKeyIssues.setModifiedUserId(user.getUserId());
        baseProductionKeyIssues.setOrgId(user.getOrganizationId());
        int i = baseProductionKeyIssuesMapper.updateByPrimaryKeySelective(baseProductionKeyIssues);

        //新增履历
        BaseHtProductionKeyIssues baseHtProductionKeyIssues = new BaseHtProductionKeyIssues();
        BeanUtils.copyProperties(baseProductionKeyIssues,baseHtProductionKeyIssues);
        baseHtProductionKeyIssuesMapper.insertSelective(baseHtProductionKeyIssues);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<BaseProductionKeyIssuesDet> baseProductionKeyIssuesDetList = baseProductionKeyIssues.getBaseProductionKeyIssuesDetList();
        if(StringUtils.isNotEmpty(baseProductionKeyIssuesDetList)) {
            for (BaseProductionKeyIssuesDet baseProductionKeyIssuesDet : baseProductionKeyIssuesDetList) {
                if (StringUtils.isNotEmpty(baseProductionKeyIssuesDet.getProductionKeyIssuesDetId())) {
                    baseProductionKeyIssuesDetMapper.updateByPrimaryKeySelective(baseProductionKeyIssuesDet);
                    idList.add(baseProductionKeyIssuesDet.getProductionKeyIssuesDetId());
                }
            }
        }

        //删除原明细
        Example example1 = new Example(BaseProductionKeyIssuesDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("productionKeyIssuesId",baseProductionKeyIssues.getProductionKeyIssuesId());
        if(idList.size()>0){
            criteria1.andNotIn("productionKeyIssuesDetId",idList);
        }
        baseProductionKeyIssuesDetMapper.deleteByExample(example1);

        //新增明细
        if (StringUtils.isNotEmpty(baseProductionKeyIssuesDetList)){
            for (BaseProductionKeyIssuesDet baseProductionKeyIssuesDet : baseProductionKeyIssuesDetList) {
                if(idList.contains(baseProductionKeyIssuesDet.getProductionKeyIssuesDetId())){
                    continue;
                }
                baseProductionKeyIssuesDet.setProductionKeyIssuesId(baseProductionKeyIssues.getProductionKeyIssuesId());
                baseProductionKeyIssuesDet.setCreateTime(new Date());
                baseProductionKeyIssuesDet.setCreateUserId(user.getUserId());
                baseProductionKeyIssuesDet.setModifiedTime(new Date());
                baseProductionKeyIssuesDet.setModifiedUserId(user.getUserId());
                baseProductionKeyIssuesDet.setOrgId(user.getOrganizationId());
                baseProductionKeyIssuesDet.setStatus(StringUtils.isEmpty(baseProductionKeyIssuesDet.getStatus())?(byte)1:baseProductionKeyIssuesDet.getStatus());
                baseProductionKeyIssuesDetMapper.insert(baseProductionKeyIssuesDet);
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtProductionKeyIssues> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseProductionKeyIssues baseProductionKeyIssues = baseProductionKeyIssuesMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseProductionKeyIssues)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtProductionKeyIssues baseHtProductionKeyIssues = new BaseHtProductionKeyIssues();
            BeanUtils.copyProperties(baseProductionKeyIssues, baseHtProductionKeyIssues);
            list.add(baseHtProductionKeyIssues);

            //删除明细
            Example example1 = new Example(BaseProductionKeyIssuesDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("productionKeyIssuesId",baseProductionKeyIssues.getProductionKeyIssuesId());
            baseProductionKeyIssuesDetMapper.deleteByExample(example1);
        }

        baseHtProductionKeyIssuesMapper.insertList(list);

        return baseProductionKeyIssuesMapper.deleteByIds(ids);
    }
}
