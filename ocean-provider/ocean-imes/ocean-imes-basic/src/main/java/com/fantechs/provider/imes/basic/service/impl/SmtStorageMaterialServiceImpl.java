package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtStorageMaterialMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStorageMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtStorageMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class SmtStorageMaterialServiceImpl  extends BaseService<SmtStorageMaterial> implements SmtStorageMaterialService {

    @Resource
    private SmtStorageMaterialMapper smtStorageMaterialMapper;

    @Resource
    private SmtHtStorageMaterialMapper smtHtStorageMaterialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtStorageMaterial smtStorageMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStorageMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageId",smtStorageMaterial.getStorageId());
        criteria.andEqualTo("materialId",smtStorageMaterial.getMaterialId());
        List<SmtStorageMaterial> smtStorageMaterials = smtStorageMaterialMapper.selectByExample(example);
        if(null!=smtStorageMaterials&&smtStorageMaterials.size()>0){
            throw new BizErrorException("该储位上的物料已存在。");
        }

        smtStorageMaterial.setCreateUserId(currentUser.getUserId());
        smtStorageMaterial.setCreateTime(new Date());
        smtStorageMaterialMapper.insertUseGeneratedKeys(smtStorageMaterial);

        //新增储位物料历史信息
        SmtHtStorageMaterial smtHtStorageMaterial=new SmtHtStorageMaterial();
        BeanUtils.copyProperties(smtStorageMaterial,smtHtStorageMaterial);
        int i = smtHtStorageMaterialMapper.insertSelective(smtHtStorageMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtStorageMaterial> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] storageMaterialIds = ids.split(",");
        for (String storageMaterialId : storageMaterialIds) {
            SmtStorageMaterial smtStorageMaterial = smtStorageMaterialMapper.selectByPrimaryKey(Long.parseLong(storageMaterialId));
            if(StringUtils.isEmpty(smtStorageMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增储位物料历史信息
            SmtHtStorageMaterial smtHtStorageMaterial=new SmtHtStorageMaterial();
            BeanUtils.copyProperties(smtStorageMaterial,smtHtStorageMaterial);
            smtHtStorageMaterial.setModifiedUserId(currentUser.getUserId());
            smtHtStorageMaterial.setModifiedTime(new Date());
            list.add(smtHtStorageMaterial);
        }
        smtHtStorageMaterialMapper.insertList(list);

        return smtStorageMaterialMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtStorageMaterial smtStorageMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStorageMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageId",smtStorageMaterial.getStorageId());
        criteria.andEqualTo("materialId",smtStorageMaterial.getMaterialId());

        SmtStorageMaterial storageMaterial = smtStorageMaterialMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(storageMaterial)&&!storageMaterial.getStorageMaterialId().equals(smtStorageMaterial.getStorageMaterialId())){
            throw new BizErrorException("该储位上的物料已存在。");
        }

        smtStorageMaterial.setModifiedUserId(currentUser.getUserId());
        smtStorageMaterial.setModifiedTime(new Date());
        int i= smtStorageMaterialMapper.updateByPrimaryKeySelective(smtStorageMaterial);

        //新增储位物料历史信息
        SmtHtStorageMaterial smtHtStorageMaterial=new SmtHtStorageMaterial();
        BeanUtils.copyProperties(smtStorageMaterial,smtHtStorageMaterial);
        smtHtStorageMaterial.setCreateUserId(smtStorageMaterial.getCreateUserId());
        smtHtStorageMaterial.setCreateTime(smtStorageMaterial.getCreateTime());
        smtHtStorageMaterial.setModifiedUserId(currentUser.getUserId());
        smtHtStorageMaterial.setModifiedTime(new Date());
        smtHtStorageMaterialMapper.insertSelective(smtHtStorageMaterial);
        return i;
    }


    @Override
    public List<SmtStorageMaterial> findList(SearchSmtStorageMaterial searchSmtStorageMaterial) {
        return smtStorageMaterialMapper.findList(searchSmtStorageMaterial);
    }
}
