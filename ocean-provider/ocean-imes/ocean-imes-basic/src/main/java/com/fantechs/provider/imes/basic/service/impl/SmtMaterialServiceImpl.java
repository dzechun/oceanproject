package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtMaterialMapper;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmtMaterialServiceImpl extends BaseService<SmtMaterial> implements SmtMaterialService {

    @Resource
    private SmtMaterialMapper smtMaterialMapper;

    @Resource
    private SmtHtMaterialMapper smtHtMaterialMapper;

    @Override
    public List<SmtMaterial> findList(SearchSmtMaterial searchSmtMaterial) {
        return smtMaterialMapper.findList(searchSmtMaterial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtMaterial smtMaterial) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode",smtMaterial.getMaterialCode());
        List<SmtMaterial> smtMaterials = smtMaterialMapper.selectByExample(example);
        if(null!=smtMaterials&&smtMaterials.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtMaterial.setCreateUserId(currentUser.getUserId());
        int i = smtMaterialMapper.insertUseGeneratedKeys(smtMaterial);

        //新增物料历史信息
        SmtHtMaterial smtHtMaterial=new SmtHtMaterial();
        BeanUtils.copyProperties(smtMaterial,smtHtMaterial);
        smtHtMaterialMapper.insertSelective(smtHtMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtMaterial smtMaterial) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode",smtMaterial.getMaterialCode());
        SmtMaterial material = smtMaterialMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(material)&&!material.getMaterialId().equals(smtMaterial.getMaterialId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtMaterial.setModifiedUserId(currentUser.getUserId());
        smtMaterial.setModifiedTime(new Date());
        int i= smtMaterialMapper.updateByPrimaryKeySelective(smtMaterial);

        //新增物料历史信息
        SmtHtMaterial smtHtMaterial=new SmtHtMaterial();
        BeanUtils.copyProperties(smtMaterial,smtHtMaterial);
        smtHtMaterial.setModifiedUserId(currentUser.getUserId());
        smtHtMaterial.setModifiedTime(new Date());
        smtHtMaterialMapper.insertSelective(smtHtMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> materialIds) {
        int i=0;
        List<SmtHtMaterial> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (Long  materialId : materialIds) {
            SmtMaterial smtMaterial = smtMaterialMapper.selectByPrimaryKey(materialId);
            if(StringUtils.isEmpty(smtMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
            //新增物料历史信息
            SmtHtMaterial smtHtMaterial=new SmtHtMaterial();
            BeanUtils.copyProperties(smtMaterial,smtHtMaterial);
            smtHtMaterial.setModifiedUserId(currentUser.getUserId());
            smtHtMaterial.setModifiedTime(new Date());
            list.add(smtHtMaterial);

            smtMaterialMapper.deleteByPrimaryKey(materialId);
        }

        i=smtHtMaterialMapper.insertList(list);
        return i;
    }

    @Override
    public SmtMaterial findById(Long materialId) {
        return smtMaterialMapper.selectByPrimaryKey(materialId);
    }
}
