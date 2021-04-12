package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductBomDetMapper;
import com.fantechs.provider.base.mapper.BaseProductBomDetMapper;
import com.fantechs.provider.base.mapper.BaseProductBomMapper;
import com.fantechs.provider.base.service.BaseProductBomDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wcz on 2020/10/12.
 */
@Service
public class BaseProductBomDetServiceImpl extends BaseService<BaseProductBomDet> implements BaseProductBomDetService {

    @Resource
    private BaseProductBomDetMapper baseProductBomDetMapper;
    @Resource
    private BaseHtProductBomDetMapper baseHtProductBomDetMapper;
    @Resource
    private BaseProductBomMapper baseProductBomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductBomDet baseProductBomDet) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }


        BaseProductBom baseProductBom = baseProductBomMapper.selectByPrimaryKey(baseProductBomDet.getProductBomId());
        if (baseProductBom.getMaterialId().equals(baseProductBomDet.getPartMaterialId())) {
            throw new BizErrorException("零件料号不能选择产品料号");
        }

        Example example = new Example(BaseProductBomDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomId", baseProductBomDet.getProductBomId());
        criteria.andEqualTo("partMaterialId", baseProductBomDet.getPartMaterialId());
        List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseProductBomDets)) {
            throw new BizErrorException("零件料号已存在");
        }

        if (baseProductBom.getMaterialId().equals(baseProductBomDet.getSubMaterialId()) || baseProductBomDet.getPartMaterialId().equals(baseProductBomDet.getSubMaterialId())) {
            throw new BizErrorException("代用料号不能选择产品料号或零件料号");
        }
        baseProductBomDet.setCreateUserId(currentUser.getUserId());
        baseProductBomDet.setCreateTime(new Date());
        baseProductBomDet.setModifiedUserId(currentUser.getUserId());
        baseProductBomDet.setModifiedTime(new Date());
        baseProductBomDetMapper.insertUseGeneratedKeys(baseProductBomDet);

        //新增产品BOM详细历史信息
        BaseHtProductBomDet baseHtProductBomDet = new BaseHtProductBomDet();
        BeanUtils.copyProperties(baseProductBomDet, baseHtProductBomDet);
        int i = baseHtProductBomDetMapper.insertSelective(baseHtProductBomDet);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductBomDet baseProductBomDet) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        BaseProductBom baseProductBom = baseProductBomMapper.selectByPrimaryKey(baseProductBomDet.getProductBomId());
        if (baseProductBom.getMaterialId().equals(baseProductBomDet.getPartMaterialId())) {
            throw new BizErrorException("零件料号不能选择产品料号");
        }

        Example example = new Example(BaseProductBomDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productBomId", baseProductBomDet.getProductBomId());
        criteria.andEqualTo("partMaterialId", baseProductBomDet.getPartMaterialId());
        BaseProductBomDet productBomDet = baseProductBomDetMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(productBomDet) && !productBomDet.getProductBomDetId().equals(productBomDet.getProductBomDetId())) {
            throw new BizErrorException("零件料号已存在");
        }

        if (baseProductBom.getMaterialId().equals(baseProductBomDet.getSubMaterialId()) || baseProductBomDet.getPartMaterialId().equals(baseProductBomDet.getSubMaterialId())) {
            throw new BizErrorException("代用料号不能选择产品料号或零件料号");
        }

        baseProductBomDet.setModifiedUserId(currentUser.getUserId());
        baseProductBomDet.setModifiedTime(new Date());
        int i = baseProductBomDetMapper.updateByPrimaryKeySelective(baseProductBomDet);

        //新增产品BOM详细历史信息
        BaseHtProductBomDet baseHtProductBomDet = new BaseHtProductBomDet();
        BeanUtils.copyProperties(baseProductBomDet, baseHtProductBomDet);
        baseHtProductBomDetMapper.insertSelective(baseHtProductBomDet);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<BaseHtProductBomDet> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] productBomDetIds = ids.split(",");
        for (String productBomDetId : productBomDetIds) {
            BaseProductBomDet baseProductBomDet = baseProductBomDetMapper.selectByPrimaryKey(productBomDetId);
            if (StringUtils.isEmpty(baseProductBomDet)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增产品BOM详细历史信息
            BaseHtProductBomDet baseHtProductBomDet = new BaseHtProductBomDet();
            BeanUtils.copyProperties(baseProductBomDet, baseHtProductBomDet);
            baseHtProductBomDet.setModifiedUserId(currentUser.getUserId());
            baseHtProductBomDet.setModifiedTime(new Date());
            list.add(baseHtProductBomDet);
        }
        baseHtProductBomDetMapper.insertList(list);

        return baseProductBomDetMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseProductBomDet> findList(SearchBaseProductBomDet searchBaseProductBomDet) {
        return baseProductBomDetMapper.findList(searchBaseProductBomDet);
    }

    @Override
    public List<BaseProductBomDet> findNextLevelProductBomDet(Long productBomDetId) {
        List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.findNextLevelProductBomDet(productBomDetId);
        return baseProductBomDets;
    }
}
