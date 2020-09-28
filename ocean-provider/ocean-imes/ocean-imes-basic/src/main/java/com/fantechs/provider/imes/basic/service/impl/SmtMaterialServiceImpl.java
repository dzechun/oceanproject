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
    public int save(SmtMaterial smtMaterial) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode",smtMaterial.getMaterialCode());
        List<SmtMaterial> smtMaterials = smtMaterialMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtMaterials)){
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
    public int update(SmtMaterial smtMaterial) {
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
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtMaterial> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr =  ids.split(",");
        for (String  materialId : idsArr) {
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
        }
        smtMaterialMapper.deleteByIds(ids);
        i=smtHtMaterialMapper.insertList(list);
        return i;
    }

}
