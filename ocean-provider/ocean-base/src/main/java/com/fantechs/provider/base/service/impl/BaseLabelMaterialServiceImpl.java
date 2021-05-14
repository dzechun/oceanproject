package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtLabelMaterialMapper;
import com.fantechs.provider.base.mapper.BaseLabelMaterialMapper;
import com.fantechs.provider.base.service.BaseLabelMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseLabelMaterialServiceImpl extends BaseService<BaseLabelMaterial> implements BaseLabelMaterialService {

         @Resource
         private BaseLabelMaterialMapper baseLabelMaterialMapper;
         @Resource
         private BaseHtLabelMaterialMapper baseHtLabelMaterialMapper;

    @Override
    public List<BaseLabelMaterialDto> findList(SearchBaseLabelMaterial searchBaseLabelMaterial) {
        return baseLabelMaterialMapper.findList(searchBaseLabelMaterial);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseLabelMaterial record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(record.getIsProcess()==(byte)1){
            throw new BizErrorException("请绑定工序");
        }
        Example example = new Example(BaseLabelMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelId",record.getLabelId());
        criteria.andEqualTo("materialId",record.getMaterialId());

        BaseLabelMaterial baseLabelMaterial = baseLabelMaterialMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabelMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        record.setOrgId(currentUserInfo.getOrganizationId());

        return baseLabelMaterialMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseLabelMaterial entity) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseLabelMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",entity.getMaterialId());
        criteria.andEqualTo("labelId",entity.getLabelId());
        criteria.andNotEqualTo("labelMaterialId",entity.getLabelMaterialId());
        BaseLabelMaterial baseLabelMaterial = baseLabelMaterialMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(baseLabelMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setOrgId(currentUserInfo.getOrganizationId());

        BaseHtLabelMaterial baseHtLabelMaterial = new BaseHtLabelMaterial();
        BeanUtils.copyProperties(entity, baseHtLabelMaterial);
        baseHtLabelMaterialMapper.insertSelective(baseHtLabelMaterial);

        return baseLabelMaterialMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtLabelMaterial> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseLabelMaterial baseLabelMaterial = baseLabelMaterialMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseLabelMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtLabelMaterial baseHtLabelMaterial = new BaseHtLabelMaterial();
            BeanUtils.copyProperties(baseLabelMaterial, baseHtLabelMaterial);
            list.add(baseHtLabelMaterial);
        }
        baseHtLabelMaterialMapper.insertList(list);

        return baseLabelMaterialMapper.deleteByIds(ids);
    }
}
