package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtKeyMaterialDto;
import com.fantechs.common.base.entity.basic.SmtKeyMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtKeyMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtKeyMaterialMapper;
import com.fantechs.provider.imes.basic.mapper.SmtKeyMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtKeyMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
public class SmtKeyMaterialServiceImpl extends BaseService<SmtKeyMaterial> implements SmtKeyMaterialService {

    @Resource
    private SmtKeyMaterialMapper smtKeyMaterialMapper;
    @Resource
    private SmtHtKeyMaterialMapper smtHtKeyMaterialMapper;

    @Override
    public List<SmtKeyMaterialDto> findList(Map<String, Object> map) {
        return smtKeyMaterialMapper.findList(map);
    }

    @Override
    public int save(SmtKeyMaterial smtKeyMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        //零件料号不能使用产品料号
        if (smtKeyMaterial.getPartMaterialId().equals(smtKeyMaterial.getMaterialId())){
            throw new BizErrorException("零件料号不能使用产品料号");
        }

        //同一工序的零件料号不能重复
        Example example = new Example(SmtKeyMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processId",smtKeyMaterial.getProcessId())
                .andEqualTo("partMaterialId",smtKeyMaterial.getPartMaterialId());
        List<SmtKeyMaterial> smtKeyMaterials = smtKeyMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtKeyMaterials)){
            throw new BizErrorException("该工序下的零件料号已存在");
        }

        smtKeyMaterial.setCreateTime(new Date());
        smtKeyMaterial.setCreateUserId(currentUser.getUserId());
        smtKeyMaterial.setModifiedUserId(currentUser.getUserId());
        smtKeyMaterial.setModifiedTime(new Date());
        smtKeyMaterial.setStatus(StringUtils.isEmpty(smtKeyMaterial.getStatus())?1:smtKeyMaterial.getStatus());
        int i = smtKeyMaterialMapper.insertUseGeneratedKeys(smtKeyMaterial);

        SmtHtKeyMaterial smtHtKeyMaterial = new SmtHtKeyMaterial();
        BeanUtils.copyProperties(smtKeyMaterial,smtHtKeyMaterial);
        smtHtKeyMaterialMapper.insertSelective(smtHtKeyMaterial);

        return i;
    }

    @Override
    public int update(SmtKeyMaterial smtKeyMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        //零件料号不能使用产品料号
        if (smtKeyMaterial.getPartMaterialId().equals(smtKeyMaterial.getMaterialId())){
            throw new BizErrorException("零件料号不能使用产品料号");
        }

        //同一工序的零件料号不能重复
        Example example = new Example(SmtKeyMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processId",smtKeyMaterial.getProcessId())
                .andEqualTo("partMaterialId",smtKeyMaterial.getPartMaterialId());
        List<SmtKeyMaterial> smtKeyMaterials = smtKeyMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtKeyMaterials)){
            throw new BizErrorException("该工序下的零件料号已存在");
        }

        smtKeyMaterial.setMaterialId(currentUser.getUserId());
        smtKeyMaterial.setModifiedTime(new Date());

        SmtHtKeyMaterial smtHtKeyMaterial = new SmtHtKeyMaterial();
        BeanUtils.copyProperties(smtKeyMaterial,smtHtKeyMaterial);
        smtHtKeyMaterialMapper.insertSelective(smtHtKeyMaterial);

        return smtKeyMaterialMapper.updateByPrimaryKeySelective(smtKeyMaterial);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] smtKeyMaterialIds = ids.split(",");
        ArrayList<SmtHtKeyMaterial> smtHtKeyMaterials = new ArrayList<>();
        for (String id : smtKeyMaterialIds) {
            SmtKeyMaterial smtKeyMaterial = smtKeyMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtKeyMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtKeyMaterial smtHtKeyMaterial = new SmtHtKeyMaterial();
            BeanUtils.copyProperties(smtKeyMaterial,smtHtKeyMaterial);
            smtHtKeyMaterial.setModifiedTime(new Date());
            smtHtKeyMaterial.setModifiedUserId(currentUser.getUserId());
            smtHtKeyMaterials.add(smtHtKeyMaterial);
        }
        smtHtKeyMaterialMapper.insertList(smtHtKeyMaterials);

        return smtKeyMaterialMapper.deleteByIds(ids);
    }
}
