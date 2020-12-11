package com.fantechs.provider.electronic.service.Impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtPaddingMaterialDto;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.SmtPaddingMaterialMapper;
import com.fantechs.provider.electronic.service.SmtPaddingMaterialService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/10.
 */
@Service
public class SmtPaddingMaterialServiceImpl extends BaseService<SmtPaddingMaterial> implements SmtPaddingMaterialService {

    @Resource
    private SmtPaddingMaterialMapper smtPaddingMaterialMapper;

    @Override
    public int save(SmtPaddingMaterial smtPaddingMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtPaddingMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("paddingMaterialCode", smtPaddingMaterial.getPaddingMaterialCode());
        List<SmtPaddingMaterial> smtPaddingMaterials = smtPaddingMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPaddingMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        return smtPaddingMaterialMapper.insertSelective(smtPaddingMaterial);
    }

    @Override
    public int update(SmtPaddingMaterial smtPaddingMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPaddingMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("paddingMaterialCode", smtPaddingMaterial.getMaterialCode())
                .andNotEqualTo("paddingMaterialId", smtPaddingMaterial.getPaddingMaterialCode());
        List<SmtPaddingMaterial> smtPaddingMaterials = smtPaddingMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPaddingMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        return smtPaddingMaterialMapper.updateByPrimaryKeySelective(smtPaddingMaterial);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtPaddingMaterial smtPaddingMaterial = smtPaddingMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtPaddingMaterial)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return smtPaddingMaterialMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtPaddingMaterialDto> findList(Map<String, Object> map) {
        return smtPaddingMaterialMapper.findList(map);
    }
}
