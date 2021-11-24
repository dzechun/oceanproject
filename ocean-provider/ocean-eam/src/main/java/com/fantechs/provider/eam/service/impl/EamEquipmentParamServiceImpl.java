package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParam;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParamList;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentParamListMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentParamMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentParamMapper;
import com.fantechs.provider.eam.service.EamEquipmentParamService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamEquipmentParamServiceImpl extends BaseService<EamEquipmentParam> implements EamEquipmentParamService {

    @Resource
    private EamEquipmentParamMapper eamEquipmentParamMapper;
    @Resource
    private EamEquipmentParamListMapper eamEquipmentParamListMapper;
    @Resource
    private EamHtEquipmentParamMapper eamHtEquipmentParamMapper;

    @Override
    public List<EamEquipmentParamDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentParamMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentParam record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamEquipmentParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCategoryId", record.getEquipmentCategoryId());
        EamEquipmentParam eamEquipmentParam = eamEquipmentParamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentParam)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentParamMapper.insertUseGeneratedKeys(record);

        //新增明细
        List<EamEquipmentParamList> equipmentParamLists = record.getList();
        if(StringUtils.isNotEmpty(equipmentParamLists)){
            for (EamEquipmentParamList eamEquipmentParamList : equipmentParamLists) {
                eamEquipmentParamList.setEquipmentParamId(record.getEquipmentParamId());
                eamEquipmentParamList.setCreateUserId(user.getUserId());
                eamEquipmentParamList.setCreateTime(new Date());
                eamEquipmentParamList.setModifiedUserId(user.getUserId());
                eamEquipmentParamList.setModifiedTime(new Date());
                eamEquipmentParamList.setStatus(StringUtils.isEmpty(eamEquipmentParamList.getStatus())?1:eamEquipmentParamList.getStatus());
                eamEquipmentParamList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentParamListMapper.insertList(equipmentParamLists);
        }

        //履历
        EamHtEquipmentParam eamHtEquipmentParam = new EamHtEquipmentParam();
        BeanUtils.copyProperties(record, eamHtEquipmentParam);
        int i = eamHtEquipmentParamMapper.insertSelective(eamHtEquipmentParam);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentParam entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamEquipmentParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCategoryId", entity.getEquipmentCategoryId())
                .andNotEqualTo("equipmentParamId",entity.getEquipmentParamId());
        EamEquipmentParam eamEquipmentParam = eamEquipmentParamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentParam)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamEquipmentParamMapper.updateByPrimaryKeySelective(entity);

        //删除原明细
        Example example1 = new Example(EamEquipmentParamList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentParamId", entity.getEquipmentParamId());
        eamEquipmentParamListMapper.deleteByExample(example1);

        //新增明细
        List<EamEquipmentParamList> equipmentParamLists = entity.getList();
        if(StringUtils.isNotEmpty(equipmentParamLists)){
            for (EamEquipmentParamList eamEquipmentParamList : equipmentParamLists) {
                eamEquipmentParamList.setEquipmentParamId(entity.getEquipmentParamId());
                eamEquipmentParamList.setCreateUserId(user.getUserId());
                eamEquipmentParamList.setCreateTime(new Date());
                eamEquipmentParamList.setModifiedUserId(user.getUserId());
                eamEquipmentParamList.setModifiedTime(new Date());
                eamEquipmentParamList.setStatus(StringUtils.isEmpty(eamEquipmentParamList.getStatus())?1:eamEquipmentParamList.getStatus());
                eamEquipmentParamList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentParamListMapper.insertList(equipmentParamLists);
        }

        //履历
        EamHtEquipmentParam eamHtEquipmentParam = new EamHtEquipmentParam();
        BeanUtils.copyProperties(entity, eamHtEquipmentParam);
        eamHtEquipmentParamMapper.insertSelective(eamHtEquipmentParam);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtEquipmentParam> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentParam eamEquipmentParam = eamEquipmentParamMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentParam)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtEquipmentParam eamHtEquipmentParam = new EamHtEquipmentParam();
            BeanUtils.copyProperties(eamEquipmentParam, eamHtEquipmentParam);
            list.add(eamHtEquipmentParam);

            //删除明细
            Example example1 = new Example(EamEquipmentParamList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentParamId", eamEquipmentParam.getEquipmentParamId());
            eamEquipmentParamListMapper.deleteByExample(example1);
        }

        eamHtEquipmentParamMapper.insertList(list);

        return eamEquipmentParamMapper.deleteByIds(ids);
    }
}
