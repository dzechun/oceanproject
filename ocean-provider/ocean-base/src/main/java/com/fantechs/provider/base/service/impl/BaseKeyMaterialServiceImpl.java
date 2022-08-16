package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseKeyMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseKeyMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtKeyMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtKeyMaterialMapper;
import com.fantechs.provider.base.mapper.BaseKeyMaterialMapper;
import com.fantechs.provider.base.service.BaseKeyMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/24.
 */
@Service
public class BaseKeyMaterialServiceImpl extends BaseService<BaseKeyMaterial> implements BaseKeyMaterialService {

    @Resource
    private BaseKeyMaterialMapper baseKeyMaterialMapper;
    @Resource
    private BaseHtKeyMaterialMapper baseHtKeyMaterialMapper;

    @Override
    public List<BaseKeyMaterialDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseKeyMaterialMapper.findList(map);
    }

    @Override
    public int save(BaseKeyMaterial baseKeyMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        //零件料号不能使用产品料号
        if (baseKeyMaterial.getPartMaterialId().equals(baseKeyMaterial.getMaterialId())){
            throw new BizErrorException("零件料号不能使用产品料号");
        }

        //同一工序的零件料号不能重复
        Example example = new Example(BaseKeyMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processId", baseKeyMaterial.getProcessId())
                .andEqualTo("partMaterialId", baseKeyMaterial.getPartMaterialId());
        List<BaseKeyMaterial> baseKeyMaterials = baseKeyMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseKeyMaterials)){
            throw new BizErrorException("该工序下的零件料号已存在");
        }

        baseKeyMaterial.setCreateTime(new Date());
        baseKeyMaterial.setCreateUserId(currentUser.getUserId());
        baseKeyMaterial.setModifiedUserId(currentUser.getUserId());
        baseKeyMaterial.setModifiedTime(new Date());
        baseKeyMaterial.setOrganizationId(currentUser.getOrganizationId());
        baseKeyMaterial.setStatus(StringUtils.isEmpty(baseKeyMaterial.getStatus())?1: baseKeyMaterial.getStatus());
        int i = baseKeyMaterialMapper.insertUseGeneratedKeys(baseKeyMaterial);

        BaseHtKeyMaterial baseHtKeyMaterial = new BaseHtKeyMaterial();
        BeanUtils.copyProperties(baseKeyMaterial, baseHtKeyMaterial);
        baseHtKeyMaterialMapper.insertSelective(baseHtKeyMaterial);

        return i;
    }

    @Override
    public int update(BaseKeyMaterial baseKeyMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        //零件料号不能使用产品料号
        if (baseKeyMaterial.getPartMaterialId().equals(baseKeyMaterial.getMaterialId())){
            throw new BizErrorException("零件料号不能使用产品料号");
        }

        //同一工序的零件料号不能重复
        Example example = new Example(BaseKeyMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processId", baseKeyMaterial.getProcessId())
                .andEqualTo("partMaterialId", baseKeyMaterial.getPartMaterialId());
        List<BaseKeyMaterial> baseKeyMaterials = baseKeyMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseKeyMaterials)){
            throw new BizErrorException("该工序下的零件料号已存在");
        }

        baseKeyMaterial.setMaterialId(currentUser.getUserId());
        baseKeyMaterial.setModifiedTime(new Date());
        baseKeyMaterial.setOrganizationId(currentUser.getOrganizationId());

        BaseHtKeyMaterial baseHtKeyMaterial = new BaseHtKeyMaterial();
        BeanUtils.copyProperties(baseKeyMaterial, baseHtKeyMaterial);
        baseHtKeyMaterialMapper.insertSelective(baseHtKeyMaterial);

        return baseKeyMaterialMapper.updateByPrimaryKeySelective(baseKeyMaterial);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] smtKeyMaterialIds = ids.split(",");
        ArrayList<BaseHtKeyMaterial> baseHtKeyMaterials = new ArrayList<>();
        for (String id : smtKeyMaterialIds) {
            BaseKeyMaterial baseKeyMaterial = baseKeyMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseKeyMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtKeyMaterial baseHtKeyMaterial = new BaseHtKeyMaterial();
            BeanUtils.copyProperties(baseKeyMaterial, baseHtKeyMaterial);
            baseHtKeyMaterial.setModifiedTime(new Date());
            baseHtKeyMaterial.setModifiedUserId(currentUser.getUserId());
            baseHtKeyMaterials.add(baseHtKeyMaterial);
        }
        baseHtKeyMaterialMapper.insertList(baseHtKeyMaterials);

        return baseKeyMaterialMapper.deleteByIds(ids);
    }
}
