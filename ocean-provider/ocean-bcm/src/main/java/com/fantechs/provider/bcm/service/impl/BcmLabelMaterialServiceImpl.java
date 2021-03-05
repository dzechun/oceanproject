package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmLabelMaterialDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.bcm.mapper.BcmHtLabelMaterialMapper;
import com.fantechs.provider.bcm.mapper.BcmLabelMaterialMapper;
import com.fantechs.provider.bcm.service.BcmLabelMaterialService;
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
public class BcmLabelMaterialServiceImpl  extends BaseService<BcmLabelMaterial> implements BcmLabelMaterialService {

         @Resource
         private BcmLabelMaterialMapper bcmLabelMaterialMapper;
         @Resource
         private BcmHtLabelMaterialMapper bcmHtLabelMaterialMapper;

    @Override
    public List<BcmLabelMaterialDto> findList(SearchBcmLabelMaterial searchBcmLabelMaterial) {
        return bcmLabelMaterialMapper.findList(searchBcmLabelMaterial);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BcmLabelMaterial record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BcmLabelMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",record.getMaterialId());
        criteria.andEqualTo("labelId",record.getLabelId());
        criteria.andEqualTo("processId",record.getProcessId());
        BcmLabelMaterial bcmLabelMaterial = bcmLabelMaterialMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabelMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());

        return bcmLabelMaterialMapper.insertUseGeneratedKeys(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BcmLabelMaterial entity) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BcmLabelMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",entity.getMaterialId());
        criteria.andEqualTo("labelId",entity.getLabelId());
        criteria.andEqualTo("processId",entity.getProcessId());
        criteria.andNotEqualTo("labelMaterialId",entity.getLabelMaterialId());
        BcmLabelMaterial bcmLabelMaterial = bcmLabelMaterialMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabelMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUserInfo.getUserId());

        BcmHtLabelMaterial bcmHtLabelMaterial = new BcmHtLabelMaterial();
        BeanUtils.copyProperties(entity,bcmHtLabelMaterial);
        bcmHtLabelMaterialMapper.insertSelective(bcmHtLabelMaterial);

        return bcmLabelMaterialMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BcmHtLabelMaterial> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BcmLabelMaterial bcmLabelMaterial = bcmLabelMaterialMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(bcmLabelMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BcmHtLabelMaterial bcmHtLabelMaterial = new BcmHtLabelMaterial();
            BeanUtils.copyProperties(bcmLabelMaterial,bcmHtLabelMaterial);
            list.add(bcmHtLabelMaterial);
        }
        bcmHtLabelMaterialMapper.insertList(list);

        return bcmLabelMaterialMapper.deleteByIds(ids);
    }
}
