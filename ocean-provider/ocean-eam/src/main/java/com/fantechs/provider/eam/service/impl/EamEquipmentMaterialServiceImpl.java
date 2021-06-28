package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterialList;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParam;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParamList;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMaterialListMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentMaterialMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaterialMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaterialService;
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
 * Created by leifengzhi on 2021/06/28.
 */
@Service
public class EamEquipmentMaterialServiceImpl extends BaseService<EamEquipmentMaterial> implements EamEquipmentMaterialService {

    @Resource
    private EamEquipmentMaterialMapper eamEquipmentMaterialMapper;
    @Resource
    private EamEquipmentMaterialListMapper eamEquipmentMaterialListMapper;
    @Resource
    private EamHtEquipmentMaterialMapper eamHtEquipmentMaterialMapper;

    @Override
    public List<EamEquipmentMaterialDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentMaterial record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", record.getEquipmentId());
        EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentMaterialMapper.insertUseGeneratedKeys(record);

        //新增明细
        List<EamEquipmentMaterialList> eamEquipmentMaterialLists = record.getList();
        if(StringUtils.isNotEmpty(eamEquipmentMaterialLists)){
            for (EamEquipmentMaterialList eamEquipmentMaterialList : eamEquipmentMaterialLists) {
                eamEquipmentMaterialList.setEquipmentMaterialId(record.getEquipmentMaterialId());
                eamEquipmentMaterialList.setCreateUserId(user.getUserId());
                eamEquipmentMaterialList.setCreateTime(new Date());
                eamEquipmentMaterialList.setModifiedUserId(user.getUserId());
                eamEquipmentMaterialList.setModifiedTime(new Date());
                eamEquipmentMaterialList.setStatus(StringUtils.isEmpty(eamEquipmentMaterialList.getStatus())?1:eamEquipmentMaterialList.getStatus());
                eamEquipmentMaterialList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentMaterialListMapper.insertList(eamEquipmentMaterialLists);
        }

        //履历
        EamHtEquipmentMaterial eamHtEquipmentMaterial = new EamHtEquipmentMaterial();
        BeanUtils.copyProperties(record, eamHtEquipmentMaterial);
        int i = eamHtEquipmentMaterialMapper.insert(eamHtEquipmentMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentMaterial entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", entity.getEquipmentId())
                .andNotEqualTo("equipmentMaterialId",entity.getEquipmentMaterialId());
        EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamEquipmentMaterialMapper.updateByPrimaryKeySelective(entity);

        //删除原明细
        Example example1 = new Example(EamEquipmentMaterialList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentMaterialId", entity.getEquipmentMaterialId());
        eamEquipmentMaterialListMapper.deleteByExample(example1);

        //新增明细
        List<EamEquipmentMaterialList> eamEquipmentMaterialLists = entity.getList();
        if(StringUtils.isNotEmpty(eamEquipmentMaterialLists)){
            for (EamEquipmentMaterialList eamEquipmentMaterialList : eamEquipmentMaterialLists) {
                eamEquipmentMaterialList.setEquipmentMaterialId(entity.getEquipmentMaterialId());
                eamEquipmentMaterialList.setCreateUserId(user.getUserId());
                eamEquipmentMaterialList.setCreateTime(new Date());
                eamEquipmentMaterialList.setModifiedUserId(user.getUserId());
                eamEquipmentMaterialList.setModifiedTime(new Date());
                eamEquipmentMaterialList.setStatus(StringUtils.isEmpty(eamEquipmentMaterialList.getStatus())?1:eamEquipmentMaterialList.getStatus());
                eamEquipmentMaterialList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentMaterialListMapper.insertList(eamEquipmentMaterialLists);
        }

        //履历
        EamHtEquipmentMaterial eamHtEquipmentMaterial = new EamHtEquipmentMaterial();
        BeanUtils.copyProperties(entity, eamHtEquipmentMaterial);
        int i = eamHtEquipmentMaterialMapper.insert(eamHtEquipmentMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtEquipmentMaterial> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentMaterial eamEquipmentMaterial = eamEquipmentMaterialMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtEquipmentMaterial eamHtEquipmentMaterial = new EamHtEquipmentMaterial();
            BeanUtils.copyProperties(eamEquipmentMaterial, eamHtEquipmentMaterial);
            list.add(eamHtEquipmentMaterial);

            //删除明细
            Example example1 = new Example(EamEquipmentMaterialList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentMaterialId", eamEquipmentMaterial.getEquipmentMaterialId());
            eamEquipmentMaterialListMapper.deleteByExample(example1);
        }

        eamHtEquipmentMaterialMapper.insertList(list);

        return eamEquipmentMaterialMapper.deleteByIds(ids);
    }
}
